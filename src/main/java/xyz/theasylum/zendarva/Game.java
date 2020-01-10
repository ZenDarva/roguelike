package xyz.theasylum.zendarva;


import xyz.theasylum.zendarva.action.*;
import xyz.theasylum.zendarva.action.Action;
import xyz.theasylum.zendarva.ai.*;
import xyz.theasylum.zendarva.ai.goal.GoalMeleeTarget;
import xyz.theasylum.zendarva.ai.goal.GoalSleep;
import xyz.theasylum.zendarva.component.*;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.domain.Floor;
import xyz.theasylum.zendarva.domain.GameState;
import xyz.theasylum.zendarva.event.EventBus;
import xyz.theasylum.zendarva.event.EventEntity;
import xyz.theasylum.zendarva.gui.GuiInventory;
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

//    public static String seed;
//    public static Random rnd;
//
//    public static Entity player;

    public static Queue<Action> actionQueue;
    public Queue<Action> playerActionQueue;
    private Queue<Integer> keyQueue;

    private GuiWindowMain gameWindow;
    private BehaviorSystem behaviorSystem = new BehaviorSystem();

    //private GameState gameState;



    public Game() {
        Window window = new Window(800, 600, "Roguelike1", this);
        this.addMouseListener(this);
        actionQueue = new ArrayDeque<>();
        playerActionQueue = new ArrayDeque<>();
        keyQueue = new ArrayDeque<>();
        this.requestFocus();
        setupGameNew();
    }


    private GameState setupGameNew(){
        GameState state = GameState.instance();
        state.seed = UUID.randomUUID().toString();
        state.rnd = new Random(stringToSeed(state.seed));

        Tileset tiles = new Tileset("/tiles3.png");
        int tilesetIndex = GameState.instance().addTileset(tiles);

        Tileset tilesEntity = new Tileset("/Humanoid0.png",16,16);
        int entityTileset = GameState.instance().addTileset(tilesEntity);

        state.addFloor(new Floor(50,30, tilesetIndex));

        Entity player = new Entity();
        player.loc = state.getCurFloor().getSpawn();
        CombatStats stats = new CombatStats(8, 8, 2);
        player.addComponent(CombatStats.class, stats);
        player.getComponent(CombatStats.class).get().setTeam(CombatStats.Team.Player);
        player.addComponent(BlocksMovement.class, new BlocksMovement());
        player.addComponent(Renderable.class, new Renderable(entityTileset,90));
        player.addComponent(Inventory.class, new Inventory());
        state.player=player;

        EventBus.instance().raiseEvent(new EventEntity.EventSpawnEntity(player));

        GuiWindowMain main = new GuiWindowMain(800, 600, 40, 30, 640, 480);
        GuiManager.instance().addWindow(main);
        gameWindow = main;

        addItems();
        addEnemies();
        EventBus.instance().update();//Hack!
        state.saveState();
        return state;
    }

    private void processActionQueue() {
        if (playerActionQueue.isEmpty())
            return;
        playerActionQueue.poll().performAction(this, GameState.instance().getCurFloor());

        processAI();

        while (!actionQueue.isEmpty()) {
            actionQueue.poll().performAction(this, GameState.instance().getCurFloor());
        }

    }

    private boolean processKeyQueue() {
    Entity player = GameState.instance().player;
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
                break;
            case KeyEvent.VK_G:
                Optional<Entity> item = GameState.instance().getCurFloor().getEntities(player.loc).stream().filter(f->f.hasComponent(Carryable.class)).findFirst();
                item.ifPresent(f->this.playerActionQueue.add(new ActionPickupItem(player,f)));
                break;
            case KeyEvent.VK_ESCAPE:
                if (GuiManager.instance().getFocusedWindow() != gameWindow){
                    GuiManager.instance().removeWindow(GuiManager.instance().getFocusedWindow());
                }
                break;
            case KeyEvent.VK_I:
                GuiInventory inventory = new GuiInventory(120,200, player);
                inventory.move(800/2-60,600/2-60);
                inventory.setVisible(true);
                GuiManager.instance().addWindow(inventory);
                break;

        }
        if (newLoc.x != -1 && newLoc.y != -1) {
            Optional<Entity> targEntity = GameState.instance().getCurFloor().getEntityWithComponent(newLoc,CombatStats.class);
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
        Optional<CombatStats> stats = GameState.instance().player.getComponent(CombatStats.class);
        stats.ifPresent(
                playerStats -> {
                    if (playerStats.getHp() <= 0) {
                        JOptionPane.showMessageDialog(null, "You Lost!");
                        actionQueue.clear();
                        keyQueue.clear();
                        GameState.instance().clear();
                        setupGameNew();
                    }
                }
        );


    }


    private void processAI() {
        behaviorSystem.update();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (GuiManager.instance().getFocusedWindow() == gameWindow || e.getKeyCode()== KeyEvent.VK_ESCAPE)
            keyQueue.add(e.getKeyCode());
        else
            GuiManager.instance().processKeystroke(e);

    }

    private void addItems(){

        for (int i = 0 ; i <10;i++) {


            Entity keyEntity = new Entity();
            keyEntity.addComponent(Carryable.class, new Carryable(1));
            //TODO: Fix this.  .get() like this is bad.
            int tilesetIndex = GameState.instance().getTilesetByFileName("/tiles3.png").get();
            keyEntity.addComponent(Renderable.class, new Renderable(tilesetIndex, GameState.instance().getCurFloor().getTileset().getNamedTilenum("key")));
            keyEntity.loc = GameState.instance().getCurFloor().getSpawn();
            keyEntity.name = "Key" + i;
            EventBus.instance().raiseEvent(new EventEntity.EventSpawnEntity(keyEntity));
        }
    }

    private void addEnemies() {
        int numEnemies = GameState.instance().rnd.nextInt(5) + 3;

        int tilesetIndex = GameState.instance().getTilesetByFileName("/Humanoid0.png").get();

        for (int i = 0; i < numEnemies; i++) {
            Entity enemy = new Entity();
            enemy.loc = GameState.instance().getCurFloor().getSpawn();
            enemy.addComponent(Behavior.class, new Behavior());
            enemy.getComponent(Behavior.class).get().addGoal(new GoalMeleeTarget());
            enemy.getComponent(Behavior.class).get().addGoal(new GoalSleep());
            CombatStats stats = new CombatStats(1,1,1);
            enemy.addComponent(CombatStats.class, stats);
            enemy.addComponent(Renderable.class,new Renderable(tilesetIndex,80));
            enemy.addComponent(BlocksMovement.class, new BlocksMovement());

            EventBus.instance().raiseEvent(new EventEntity.EventSpawnEntity(enemy));
        }

        for (int i = 0; i < numEnemies / 3; i++) {
            Entity enemy = new Entity();
            enemy.loc = GameState.instance().getCurFloor().getSpawn();
            enemy.addComponent(Behavior.class, new Behavior());
            enemy.getComponent(Behavior.class).get().addGoal(new GoalMeleeTarget());
            enemy.getComponent(Behavior.class).get().addGoal(new GoalSleep());
            CombatStats stats = new CombatStats(3,3,2);
            enemy.addComponent(CombatStats.class, stats);
            enemy.addComponent(Renderable.class,new Renderable(tilesetIndex,2));
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
