package xyz.theasylum.zendarva.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;
import xyz.theasylum.zendarva.Tileset;
import xyz.theasylum.zendarva.component.Component;
import xyz.theasylum.zendarva.event.EventAddMessage;
import xyz.theasylum.zendarva.event.EventBus;
import xyz.theasylum.zendarva.event.EventEntity;
import xyz.theasylum.zendarva.gui.GuiMessageLog;
import xyz.theasylum.zendarva.serialization.ComponentSerializer;
import xyz.theasylum.zendarva.serialization.TilesetSerializer;

import java.io.*;
import java.util.*;

public class GameState {
    private ArrayList<Floor> floors;
    public Entity player;
    private HashMap<Integer, Set<Entity>> entities;
    public Random rnd;
    public String seed;
    private int curFloor =-1;
    private String messageLog;

    private List<Tileset> tileSets;


    private static GameState myInstance;

    public void clear(){
        myInstance= new GameState();
    }

    private GameState(){
        EventBus.instance().registerHandler(this);
        floors = new ArrayList<>();
        entities= new HashMap<>();
        tileSets=new ArrayList<>();
        messageLog = "";
    }

    public static GameState instance(){
        if (myInstance == null){
            myInstance= new GameState();
        }
        return myInstance;
    }

    public int  addTileset(Tileset tileset){
        int newIndex = tileSets.size();
        tileSets.add(newIndex, tileset);
        return newIndex;
    }

    public Tileset getTilest(int index){
        return tileSets.get(index);
    }

    public Optional<Integer> getTilesetByFileName(String filename){
        Optional<Tileset> set = tileSets.stream().filter(f-> f.filename.equals(filename)).findFirst();
        return set.map(tileset -> tileSets.indexOf(tileset));
    }

    /*Changes will be handled by event, shouldn't be changed manually.*/
    public Floor getCurFloor(){
        return floors.get(curFloor);
    }

    public void addFloor(Floor floor){
        int newIndex = floors.size();
        floors.add(newIndex,floor);
        entities.put(newIndex, new HashSet<Entity>());
        if (curFloor == -1){
            curFloor=newIndex;
        }
    }

    public Set<Entity> getEntitiesForFloor(Floor floor){
        return getEntitiesForFloor(floors.indexOf(floor));
    }

    public Set<Entity> getEntitiesForFloor(int floor){
        return entities.get(floor);
    }

    public void addMessage(String message){
        messageLog+="\r\n"+message;
        EventBus.instance().raiseEvent(new EventAddMessage(message));
    }
    public String getMessageLog(){
        return messageLog;
    }

    private void handleSpawnEnemey(EventEntity.EventSpawnEntity e){
        Set<Entity> set = entities.get(curFloor);
        set.add(e.getEntity());
    }

    private void handleMobDeath(EventEntity.EventEntityDie e){
        Set<Entity> set = entities.get(curFloor);
        set.remove(e.getEntity());
    }

    private void handleEntityTakeItem(EventEntity.EventEntityTakeItem e){
        entities.get(curFloor).remove(e.getItem());
    }

    public void saveState(){
        Gson gson = getGsonSerializer();

        String str= gson.toJson(this);
        File file = new File("/temp/world.json");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(str.getBytes());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    public GameState loadState(){
        Gson gson = getGsonSerializer();
        File file = new File("/temp/world.json");

        try (FileInputStream fis = new FileInputStream(file)) {
            InputStreamReader reader = new InputStreamReader(fis);
            GameState state = gson.fromJson(new JsonReader(reader), GameState.class);
            myInstance.clear();
            myInstance=state;
            return state;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private Gson getGsonSerializer(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Tileset.class,new TilesetSerializer());
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(Component.class, new ComponentSerializer());
        return builder.create();


    }





}
