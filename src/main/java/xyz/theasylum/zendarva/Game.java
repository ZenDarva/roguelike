package xyz.theasylum.zendarva;


import xyz.theasylum.zendarva.action.*;
import xyz.theasylum.zendarva.action.Action;
import xyz.theasylum.zendarva.ai.Behavior;
import xyz.theasylum.zendarva.ai.BehaviorSmartZombie;
import xyz.theasylum.zendarva.ai.BehaviorZombie;
import xyz.theasylum.zendarva.component.*;
import xyz.theasylum.zendarva.component.Component;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.util.Queue;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {
    private boolean isRunning = true;

    public static String seed;
    public static Random rnd;

    public static Entity player;

    public Queue<Action> actionQueue;
    public Queue<Action> playerActionQueue;
    private Queue<Integer> keyQueue;

    private GuiWindowMain gameWindow;

    //bad!
    private Tileset entityTileset;

    public Game() {
        Window window = new Window(800, 600, "Roguelike1", this);
        this.addMouseListener(this);
        actionQueue = new ArrayDeque<>();
        playerActionQueue = new ArrayDeque<>();
        keyQueue = new ArrayDeque<>();
        this.requestFocus();
        entityTileset = new Tileset("/Humanoid0.png",16,16);
        setupGameNew();
    }

    private void setupGameNew() {
        seed = UUID.randomUUID().toString();
        rnd = new Random(stringToSeed(seed));
        Tileset tiles = new Tileset("/tiles3.png");
        player = new Entity();

        GuiWindowMain main = new GuiWindowMain(800, 600, 40, 30, 640, 480, tiles);
        GuiManager.instance().addWindow(main);
        gameWindow = main;

        player.loc = gameWindow.getCurrentFloor().getSpawn();
        CombatStats stats = new CombatStats(8, 8, 2);
        player.addComponent(CombatStats.class, stats);
        player.addComponent(BlocksMovement.class, new BlocksMovement());
        player.addComponent(Renderable.class, new Renderable(entityTileset,90));
        player.addComponent(Inventory.class, new Inventory(player));

        EventBus.instance().raiseEvent(new EventEntity.EventSpawnEntity(player));

        addEnemies();
        addItems();

    }

    private void processActionQueue() {
        if (playerActionQueue.isEmpty())
            return;
        playerActionQueue.poll().performAction(this, gameWindow.getCurrentFloor());

        processAI();

        while (!actionQueue.isEmpty()) {
            actionQueue.poll().performAction(this, gameWindow.getCurrentFloor());
        }

    }

    private boolean processKeyQueue() {
        if (keyQueue.isEmpty())
            return false;
        Integer keycode = keyQueue.poll();
        final Point newLoc = new Point(-1, -1);
        switch (keycode) {
            case KeyEvent.VK_UP:
                newLoc.setLocation(player.loc.x, player.loc.y - 1);
                break;
            case KeyEvent.VK_DOWN:
                newLoc.setLocation(player.loc.x, player.loc.y + 1);
                break;
            case KeyEvent.VK_LEFT:
                newLoc.setLocation(player.loc.x - 1, player.loc.y);
                break;
            case KeyEvent.VK_RIGHT:
                newLoc.setLocation(player.loc.x + 1, player.loc.y);
                break;
            case KeyEvent.VK_SPACE:
                this.playerActionQueue.add(new ActionWait(player));
                return true;
            case KeyEvent.VK_S:
                player.getComponent(CombatStats.class).ifPresent(f -> f.setHp(0));
            case KeyEvent.VK_G:
                Optional<Entity> item = gameWindow.getCurrentFloor().getEntities(player.loc).stream().filter(f->f.hasComponent(Carryable.class)).findFirst();
                item.ifPresent(f->this.playerActionQueue.add(new ActionPickupItem(player,f)));

        }
        if (newLoc.x != -1 && newLoc.y != -1) {
            Optional<Entity> targEntity = gameWindow.getCurrentFloor().getEntityWithComponent(newLoc,CombatStats.class);
            targEntity.ifPresentOrElse(f -> this.playerActionQueue.add(new ActionAttackEntity(player, f)),
                    () -> this.playerActionQueue.add(new ActionMoveEntity(player, newLoc)));
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        gameLoop();
    }

    public void gameLoop() {
        long nextTick = 0;
        long nextFrame = 0;
        while (isRunning) {
            long curTime = System.currentTimeMillis();

            if (curTime > nextTick) {
                processActionQueue();
                processKeyQueue();
                EventBus.instance().update();
                checkGameOver();
                nextTick = curTime + 50;
            }

            if (curTime > nextFrame) {

                nextFrame = curTime + 34;
                BufferStrategy strat = getBufferStrategy();
                if (strat == null) {
                    createBufferStrategy(2);
                    strat = getBufferStrategy();
                }

                Graphics g = strat.getDrawGraphics();

                g.setColor(Color.BLACK);
                g.fillRect(0, 0, 800, 600);

                GuiManager.instance().update();
                GuiManager.instance().draw(g);

                g.dispose();
                strat.show();
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkGameOver() {
        Optional<CombatStats> stats = player.getComponent(CombatStats.class);
        stats.ifPresent(
                playerStats -> {
                    if (playerStats.getHp() <= 0) {
                        JOptionPane.showMessageDialog(null, "You Lost!");
                        actionQueue.clear();
                        keyQueue.clear();
                        setupGameNew();
                    }
                }
        );


    }


    private void processAI() {
        for (Entity entity : gameWindow.getCurrentFloor().getEntities()) {
            entity.getComponent(Behavior.class).ifPresent(f -> processBehavior((Behavior) f));
        }
    }

    private void processBehavior(Behavior behavior) {
        behavior.execute(gameWindow.getCurrentFloor(), this).ifPresent(f -> actionQueue.add(f));
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

    private void addItems(){

        Entity keyEntity = new Entity();
        keyEntity.addComponent(Carryable.class, new Carryable(1));
        keyEntity.addComponent(Renderable.class, new Renderable(gameWindow.getCurrentFloor().getTileset(),gameWindow.getCurrentFloor().getTileset().getNamedTilenum("key")));
        keyEntity.loc= gameWindow.getCurrentFloor().getSpawn();
        EventBus.instance().raiseEvent(new EventEntity.EventSpawnEntity(keyEntity));
    }

    private void addEnemies() {
        int numEnemies = Game.rnd.nextInt(5) + 3;

        for (int i = 0; i < numEnemies; i++) {
            Entity enemy = new Entity();
            enemy.loc = gameWindow.getCurrentFloor().getSpawn();
            enemy.tileNum = 1;
            enemy.addComponent(Behavior.class, new BehaviorZombie(enemy));

            CombatStats stats = new CombatStats(1,1,1);
            enemy.addComponent(CombatStats.class, stats);

            enemy.addComponent(Renderable.class,new Renderable(entityTileset,80));
            enemy.addComponent(BlocksMovement.class, new BlocksMovement());

            EventBus.instance().raiseEvent(new EventEntity.EventSpawnEntity(enemy));
        }

        for (int i = 0; i < numEnemies / 3; i++) {
            Entity enemy = new Entity();
            enemy.loc = gameWindow.getCurrentFloor().getSpawn();
            enemy.tileNum = 2;
            enemy.addComponent(Behavior.class, new BehaviorSmartZombie(enemy));
            CombatStats stats = new CombatStats(3,3,2);
            enemy.addComponent(CombatStats.class, stats);
            enemy.addComponent(Renderable.class,new Renderable(entityTileset,0));
            enemy.addComponent(BlocksMovement.class, new BlocksMovement());

            EventBus.instance().raiseEvent(new EventEntity.EventSpawnEntity(enemy));
        }

    }



    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    //utils.
    static long stringToSeed(String s) {
        if (s == null) {
            return 0;
        }
        long hash = 0;
        for (char c : s.toCharArray()) {
            hash = 31L * hash + c;
        }
        return hash;
    }
}
