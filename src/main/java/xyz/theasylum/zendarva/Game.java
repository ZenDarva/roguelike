package xyz.theasylum.zendarva;

import xyz.theasylum.zendarva.actions.Action;
import xyz.theasylum.zendarva.actions.ActionMoveEntity;
import xyz.theasylum.zendarva.ai.BehaviorWander;

import java.awt.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.*;

public class Game extends Canvas implements Runnable, KeyListener {
    private boolean isRunning = true;
    private ArrayList<IDrawable> drawables = new ArrayList<>();

    public static String seed;
    public static Random rnd;

    private Entity player;

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
        map.addEntity(player);
        addEnemies();
        drawables.add(map);
    }

    private void processActionQueue(){
        while (!actionQueue.isEmpty()){
            actionQueue.poll().performAction(this,map);
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
            enemy.components.add(new BehaviorWander(enemy));
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
