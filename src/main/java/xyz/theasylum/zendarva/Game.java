package xyz.theasylum.zendarva;

import xyz.theasylum.zendarva.actions.Action;
import xyz.theasylum.zendarva.actions.ActionMoveEntity;

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

    Map map;

    public Game(){
        Window window =new Window(800,600,"Roguelike1",this);
        actionQueue = new ArrayDeque<>();
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
        if (!actionQueue.isEmpty()){
            actionQueue.poll().performAction(this,map);
        }
    }


    @Override
    public void run() {
        while (isRunning){

            processActionQueue();

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

        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
                this.actionQueue.add(new ActionMoveEntity(player, new Point(player.loc.x,player.loc.y-1)));

                break;
            case KeyEvent.VK_DOWN:
                this.actionQueue.add(new ActionMoveEntity(player, new Point(player.loc.x,player.loc.y+1)));
                break;
            case KeyEvent.VK_LEFT:
                this.actionQueue.add(new ActionMoveEntity(player, new Point(player.loc.x-1,player.loc.y)));
                break;
            case KeyEvent.VK_RIGHT:
                this.actionQueue.add(new ActionMoveEntity(player, new Point(player.loc.x+1,player.loc.y)));
                break;

        }
    }

    private void addEnemies(){
        int numEnemies = Game.rnd.nextInt(5)+3;

        for (int i = 0; i < numEnemies; i++) {
            Entity enemy = new Entity();
            enemy.loc=map.getSpawn();
            enemy.tileNum=1;
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
