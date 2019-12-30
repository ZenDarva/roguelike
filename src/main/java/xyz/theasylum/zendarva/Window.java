package xyz.theasylum.zendarva;

import javax.swing.*;
import java.awt.*;

public class Window {

    public Window (int width, int height, String title, Game game){
        JFrame frame = new JFrame(title);
        frame.setSize(new Dimension(width,height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.setVisible(true);
        game.addKeyListener(game);

    }





}
