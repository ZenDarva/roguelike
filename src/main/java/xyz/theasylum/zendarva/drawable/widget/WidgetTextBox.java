package xyz.theasylum.zendarva.drawable.widget;

import xyz.theasylum.zendarva.gui.GuiWindow;

import java.awt.*;

public class WidgetTextBox extends Widget {
    private final int width;
    private final int height;
    private String text="";


    public WidgetTextBox(GuiWindow parent, int width, int height) {
        super(parent, width, height);
        this.width = width;
        this.height = height;

    }

    @Override
    protected void drawForeground(Graphics g) {
        String[] lines = text.split("[\r\n]+");
        int x = 10;
        int y = 10;
        for (String line : lines) {
            drawSmallString(x,y,line);
            y+=g.getFontMetrics().getHeight()+2;
        }
    }


    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }


    @Override
    public void update() {

    }
}
