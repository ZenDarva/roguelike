package xyz.theasylum.zendarva.drawable.widget;

import xyz.theasylum.zendarva.ITickable;
import xyz.theasylum.zendarva.gui.GuiWindow;

import java.awt.*;

public class WidgetPopupText extends Widget implements ITickable {
    private int duration;
    private final String text;

    public WidgetPopupText(IWidgetContainer parent, int duration, String text) {
        super(parent,64,32);
        this.duration = duration;
        this.text = text;
        this.zDepth=10;
        this.setOpaque(false);
    }

    @Override
    protected void drawForeground(Graphics g) {
        g.setColor(Color.red);

        g.setFont(smallFont.deriveFont(Font.BOLD).deriveFont(12f));
        int width = g.getFontMetrics().stringWidth(text);
        g.drawString(text,width/2,16);
    }

    @Override
    public void update() {
        duration--;
        if (duration == 0) {
            parent.removeWidget(this);
            parent=null;
        }
    }
}
