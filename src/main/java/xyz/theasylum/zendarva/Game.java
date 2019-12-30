package xyz.theasylum.zendarva;

import java.awt.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Game extends Canvas implements Runnable, KeyListener {
    private boolean isRunning = true;
    private ArrayList<IDrawable> drawables = new ArrayList<>();

    public static String seed;
    public static Random rnd;

    private Entity player;

    Map map;

    public Game(){
        Window window =new Window(800,600,"Roguelike1",this);
        this.requestFocus();
        seed = UUID.randomUUID().toString();
        rnd = new Random(stringToSeed(seed));
        map = new Map(40,30);
        player = new Entity();
        player.loc= map.getSpawn();
        map.addEntity(player);
        drawables.add(map);
    }


    @Override
    public void run() {
        while (isRunning){
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
                map.moveEntity(player,player.loc.x,player.loc.y-1);
                break;
            case KeyEvent.VK_DOWN:
                map.moveEntity(player,player.loc.x,player.loc.y+1);
                break;
            case KeyEvent.VK_LEFT:
                map.moveEntity(player,player.loc.x-1,player.loc.y);
                break;
            case KeyEvent.VK_RIGHT:
                map.moveEntity(player,player.loc.x+1,player.loc.y);
                break;
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
