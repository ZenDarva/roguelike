package xyz.theasylum.zendarva;


import xyz.theasylum.zendarva.action.Action;
import xyz.theasylum.zendarva.action.ActionAttackEntity;
import xyz.theasylum.zendarva.action.ActionMoveEntity;
import xyz.theasylum.zendarva.action.ActionWait;
import xyz.theasylum.zendarva.ai.Behavior;
import xyz.theasylum.zendarva.ai.BehaviorFastZombie;
import xyz.theasylum.zendarva.ai.BehaviorZombie;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.drawable.IDrawable;
import xyz.theasylum.zendarva.drawable.widget.Widget;
import xyz.theasylum.zendarva.event.EventBus;
import xyz.theasylum.zendarva.event.EventEntity;
import xyz.theasylum.zendarva.gui.GuiManager;
import xyz.theasylum.zendarva.gui.GuiWindowMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class Game extends Canvas implements Runnable, KeyListener {
    private boolean isRunning = true;
    private ArrayList<IDrawable> drawables = new ArrayList<>();
    private ArrayList<Widget> widgets = new ArrayList<>();

    public static String seed;
    public static Random rnd;

    public static Entity player;

    public Queue<Action> actionQueue;
    private Queue<Integer> keyQueue;

    private GuiWindowMain gameWindow;

    public Game(){
        Window window =new Window(800,600,"Roguelike1",this);
        actionQueue = new ArrayDeque<>();
        keyQueue = new ArrayDeque<>();
        this.requestFocus();
        setupGameNew();
    }

    private void setupGameNew(){
        seed = UUID.randomUUID().toString();
        rnd = new Random(stringToSeed(seed));
        Tileset tiles = new Tileset("/tiles.png",16,16);
        player = new Entity();

        GuiWindowMain main = new GuiWindowMain(800, 600,40,30, 640,480,tiles);
        GuiManager.instance().addWindow(main);
        gameWindow=main;

        player.loc= gameWindow.getCurrentFloor().getSpawn();
        player.hp=8;
        player.maxHp=8;

        EventBus.instance().raiseEvent(new EventEntity.EventSpawnEntity(player));

        addEnemies();

    }

    private void processActionQueue(){
        boolean playerActed = false;
        while (!actionQueue.isEmpty()){
            if (actionQueue.peek().performedBy() == player){
               playerActed=true;
            }
            actionQueue.poll().performAction(this,gameWindow.getCurrentFloor());
        }

        if (playerActed){
            processAI();
        }
    }
    private boolean processKeyQueue(){
        if (keyQueue.isEmpty())
                return false;
        Integer keycode = keyQueue.poll();
        final Point newLoc = new Point(-1,-1);
        switch(keycode){
            case KeyEvent.VK_UP:
                newLoc.setLocation(player.loc.x,player.loc.y-1);
                this.actionQueue.add(new ActionMoveEntity(player, newLoc));
                break;
            case KeyEvent.VK_DOWN:
                newLoc.setLocation(player.loc.x,player.loc.y+1);
                this.actionQueue.add(new ActionMoveEntity(player, newLoc));
                break;
            case KeyEvent.VK_LEFT:
                newLoc.setLocation(player.loc.x-1,player.loc.y);
                this.actionQueue.add(new ActionMoveEntity(player, newLoc));
                break;
            case KeyEvent.VK_RIGHT:
                newLoc.setLocation(player.loc.x+1,player.loc.y);
                this.actionQueue.add(new ActionMoveEntity(player, newLoc));
                break;
            case KeyEvent.VK_SPACE:
                this.actionQueue.add(new ActionWait(player));
                return true;
            case KeyEvent.VK_S:
                player.hp=0;
        }
        if (newLoc.x !=-1 && newLoc.y !=-1){
            Optional<Entity> targEntity = gameWindow.getCurrentFloor().getEntity(newLoc);
            targEntity.ifPresentOrElse(f->this.actionQueue.add(new ActionAttackEntity(player,f)),
                    ()->this.actionQueue.add(new ActionMoveEntity(player, newLoc)));
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        while (isRunning){

            processActionQueue();
            processKeyQueue();
            EventBus.instance().update();

            checkGameOver();

            BufferStrategy strat = getBufferStrategy();
            if (strat == null){
                createBufferStrategy(2);
                strat =getBufferStrategy();
            }

            Graphics g = strat.getDrawGraphics();

            g.setColor(Color.BLACK);
            g.fillRect(0,0,800,600);

            //drawables.forEach(f->f.draw(g));
            GuiManager.instance().update();
            GuiManager.instance().draw(g);

            drawUI(g);

            g.dispose();
            strat.show();
        }
    }

    private void checkGameOver() {
        if (player.hp<=0){
            JOptionPane.showMessageDialog(null,"You Lost!");
            actionQueue.clear();
            widgets.clear();
            keyQueue.clear();
            drawables.clear();
            setupGameNew();
        }
    }


    //Bad.
    private void processAI() {
        for (Entity entity : gameWindow.getCurrentFloor().getEntities()) {
            List<Behavior> behave = entity.components.stream().filter(f->f instanceof Behavior).map(f->(Behavior)f).collect(Collectors.toList());
            for (Behavior behavior : behave) {
                Optional<Action> action = behavior.execute(gameWindow.getCurrentFloor(),this);
                action.ifPresent(f->actionQueue.add(f));
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

        keyQueue.add(e.getKeyCode());

    }

    private void addEnemies(){
        int numEnemies = Game.rnd.nextInt(5)+3;

        for (int i = 0; i < numEnemies; i++) {
            Entity enemy = new Entity();
            enemy.loc=gameWindow.getCurrentFloor().getSpawn();
            enemy.tileNum=1;
            enemy.components.add(new BehaviorZombie(enemy));
            enemy.hp =1;
            enemy.maxHp=1;
            EventBus.instance().raiseEvent(new EventEntity.EventSpawnEntity(enemy));
        }

        for (int i = 0; i < numEnemies/3; i++) {
            Entity enemy = new Entity();
            enemy.loc=gameWindow.getCurrentFloor().getSpawn();
            enemy.tileNum=2;
            enemy.components.add(new BehaviorFastZombie(enemy));
            enemy.hp =3;
            enemy.maxHp=3;
            EventBus.instance().raiseEvent(new EventEntity.EventSpawnEntity(enemy));
        }

    }

    private void drawUI(Graphics g){
        widgets.stream().filter(Widget::getVisible).forEach(f->f.draw(g));
    }

    private void addWidget(Widget widget){
        widgets.add(widget);
    }

    //utils.
    static long stringToSeed(String s) {
        if (s == null) {
            return 0;
        }
        long hash = 0;
        for (char c : s.toCharArray()) {
            hash = 31L*hash + c;
        }
        return hash;
    }


}
