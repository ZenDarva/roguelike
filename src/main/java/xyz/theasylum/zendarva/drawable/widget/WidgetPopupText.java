package xyz.theasylum.zendarva.drawable.widget;

import xyz.theasylum.zendarva.ITickable;
import xyz.theasylum.zendarva.gui.GuiWindow;

import java.awt.*;

public class WidgetPopupText extends Widget implements ITickable {
    private int duration;
    private final String text;

    public WidgetPopupText(GuiWindow parent, int duration, String text) {
        super(parent);
        this.duration = duration;
        this.text = text;
        this.zDepth=10;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);

        g.setFont(smallFont.deriveFont(Font.BOLD).deriveFont(12f));
        int width = g.getFontMetrics().stringWidth(text);
        g.drawString(text,loc.x-width/2,loc.y);
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
