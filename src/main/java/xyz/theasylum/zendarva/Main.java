package xyz.theasylum.zendarva;

import xyz.theasylum.zendarva.event.Event;
import xyz.theasylum.zendarva.event.EventBus;

public class Main {

    public static void main(String args[]){
        Game game = new Game();
        Thread thread = new Thread(game);
        Image image = new Image();
        image.load();
        thread.start();

    }




}
