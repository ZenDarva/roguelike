package xyz.theasylum.zendarva;

public class Main {

    public static void main(String args[]){
        Game game = new Game();
        Thread thread = new Thread(game);
        Image image = new Image();
        image.load();
        thread.start();

    }
}
