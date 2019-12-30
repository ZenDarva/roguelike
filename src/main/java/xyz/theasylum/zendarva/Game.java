package xyz.theasylum.zendarva;


import xyz.theasylum.zendarva.actions.Action;
import xyz.theasylum.zendarva.actions.ActionMoveEntity;
import xyz.theasylum.zendarva.ai.Behavior;
import xyz.theasylum.zendarva.ai.BehaviorWander;
import xyz.theasylum.zendarva.ai.BehaviorZombie;

import java.awt.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Game extends Canvas implements Runnable, KeyListener {
    private boolean isRunning = true;
    private ArrayList<IDrawable> drawables = new ArrayList<>();

    public static String seed;
    public static Random rnd;

    public Entity player;

    public Queue<Action> actionQueue;
    private Queue<Integer> keyQueue;

    Map map;

    public Game(){
        Window window =new Window(800,600,"Roguelike1",this);
        actionQueue = new ArrayDeque<>();
        keyQueue = new ArrayDeque<>();
        this.requestFocus();
        seed = UUID.randomUUID().toString();
        rnd = new Random(stringToSeed(seed));
        map = new Map(40,30);
        player = new Entity();
        player.loc= map.getSpawn();
        player.hp=5;
        player.maxHp=5;
        map.addEntity(player);
        addEnemies();
        drawables.add(map);
    }

    private void processActionQueue(){
        boolean playerActed = false;
        while (!actionQueue.isEmpty()){
            if (actionQueue.peek().performedBy() == player){
               playerActed=true;
            }
            actionQueue.poll().performAction(this,map);
        }

        if (playerActed){
            processAI();
        }
    }
    private boolean processKeyQueue(){
        if (keyQueue.isEmpty())
                return false;
        Integer keycode = keyQueue.poll();
        switch(keycode){
            case KeyEvent.VK_UP:
                this.actionQueue.add(new ActionMoveEntity(player, new Point(player.loc.x,player.loc.y-1)));
                return true;
            case KeyEvent.VK_DOWN:
                this.actionQueue.add(new ActionMoveEntity(player, new Point(player.loc.x,player.loc.y+1)));
                return true;
            case KeyEvent.VK_LEFT:
                this.actionQueue.add(new ActionMoveEntity(player, new Point(player.loc.x-1,player.loc.y)));
                return true;
            case KeyEvent.VK_RIGHT:
                this.actionQueue.add(new ActionMoveEntity(player, new Point(player.loc.x+1,player.loc.y)));
                return true;
        }
        return false;
    }

    @Override
    public void run() {
        while (isRunning){

            processActionQueue();
            processKeyQueue();

            BufferStrategy strat = getBufferStrategy();
            if (strat == null){
                createBufferStrategy(2);
                strat =getBufferStrategy();
            }

            Graphics g = strat.getDrawGraphics();

            g.setColor(Color.BLACK);
            g.fillRect(0,0,800,600);

            drawables.forEach(f->f.draw(g));

            g.dispose();
            strat.show();
        }
    }


    //Bad.
    private void processAI() {
        for (Entity entity : map.entities) {
            List<Behavior> behave = entity.components.stream().filter(f->f instanceof Behavior).map(f->(Behavior)f).collect(Collectors.toList());
            for (Behavior behavior : behave) {
                Optional<Action> action = behavior.execute(map,this);
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
            enemy.loc=map.getSpawn();
            enemy.tileNum=1;
            enemy.components.add(new BehaviorZombie(enemy));
            enemy.hp =2;
            enemy.maxHp=2;
            map.addEntity(enemy);
        }

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
