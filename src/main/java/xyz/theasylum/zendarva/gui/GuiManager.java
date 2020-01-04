package xyz.theasylum.zendarva.gui;

import xyz.theasylum.zendarva.ITickable;
import xyz.theasylum.zendarva.drawable.IDrawable;
import xyz.theasylum.zendarva.drawable.widget.Widget;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class GuiManager implements IDrawable, ITickable {

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
        if (focusedWindow !=null){
            window.setZ(focusedWindow.getZ()+1);
        }
        focusedWindow= window;
        Collections.sort(windows, Comparator.comparingInt(GuiWindow::getZ));
    }
    public void removeWindow(GuiWindow window) {
        windows.remove(window);
        Collections.sort(windows, Comparator.comparingInt(GuiWindow::getZ));
        if (focusedWindow==window){
            focusedWindow= windows.get(0);
        }
    }


    @Override
    public void draw(Graphics g) {
        windows.stream().forEach(f->f.draw(g));
    }

    @Override
    public void update() {
        for (GuiWindow window : windows) {
            window.update();
        }
    }
    public GuiWindow getFocusedWindow() {
        return focusedWindow;
    }

    public void processKeystroke(KeyEvent event){
        if (focusedWindow!=null)
            focusedWindow.processKeystroke(event);
    }
}
