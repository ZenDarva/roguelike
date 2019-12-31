package xyz.theasylum.zendarva.gui;

import xyz.theasylum.zendarva.drawable.IDrawable;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class GuiManager implements IDrawable {

    private List<GuiWindow> windows;

    private GuiWindow focusedWindow = null;

    private static GuiManager myInstance;

    private GuiManager(){
        windows = new LinkedList<>();
    }

    public static GuiManager instance(){
        if (myInstance == null){
            myInstance= new GuiManager();
        }
        return myInstance;
    }

    public void addWindow(GuiWindow window) {
        windows.add(window);
        focusedWindow= window;
    }
    public void removeWindow(GuiWindow window) {
        windows.remove(window);
        if (focusedWindow==window){
            focusedWindow= windows.get(0);
        }
    }


    @Override
    public void draw(Graphics g) {
        windows.stream().forEach(f->f.draw(g));
    }
}
