package xyz.theasylum.zendarva.drawable.widget;

import xyz.theasylum.zendarva.Entity;

import java.awt.*;

public class WidgetStat extends Widget {

    private Entity entity;

    public WidgetStat(Entity entity) {

        this.entity = entity;
    }

    @Override
    public void draw(Graphics g) {
        drawSmallString(g,10,10,"Hitpoints");
        drawRect(g,10,15,70,10, false);
        float percent = (float)entity.hp/(float)entity.maxHp;
        percent = percent * 100;
        percent = (percent * 70f)/100;
        drawRect(g,10,15,(int)percent,10,true);
//        drawSmallString(g,10,35,""+percent);
//        drawSmallString(g,10,50,"" + entity.hp);
    }


    @Override
    public void update() {

    }
}
