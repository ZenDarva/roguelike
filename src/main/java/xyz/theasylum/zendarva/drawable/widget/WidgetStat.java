package xyz.theasylum.zendarva.drawable.widget;

import xyz.theasylum.zendarva.component.CombatStats;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.gui.GuiWindow;

import java.awt.*;

public class WidgetStat extends Widget {

    private Entity entity;

    public WidgetStat(GuiWindow parent, Entity entity, int width, int height) {
        super(parent,width,height);
        this.entity = entity;
        this.visible = true;
    }

    @Override
    protected void drawForeground(Graphics g) {
        CombatStats stats = entity.getComponent(CombatStats.class).get();
        drawSmallString(10,10,"Hitpoints");
        drawRect(10,15,70,10, false);
        float percent = (float)stats.getHp()/(float)stats.getMaxHp();
        percent = percent * 100;
        percent = (percent * 70f)/100;
        drawRect(10,15,(int)percent,10,true);
    }

    @Override
    public void update() {

    }
}
