package xyz.theasylum.zendarva.drawable.widget;

import xyz.theasylum.zendarva.component.CombatStats;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.gui.GuiWindow;

import java.awt.*;

public class WidgetStat extends Widget {

    private Entity entity;

    public WidgetStat(GuiWindow parent, Entity entity) {
        super(parent);
        this.entity = entity;
    }

    @Override
    public void draw(Graphics g) {
        CombatStats stats = entity.getComponent(CombatStats.class).get();
        drawSmallString(g,10,10,"Hitpoints");
        drawRect(g,10,15,70,10, false);
        float percent = (float)stats.getHp()/(float)stats.getMaxHp();
        percent = percent * 100;
        percent = (percent * 70f)/100;
        drawRect(g,10,15,(int)percent,10,true);
    }


    @Override
    public void update() {

    }
}
