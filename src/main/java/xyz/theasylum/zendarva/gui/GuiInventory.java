package xyz.theasylum.zendarva.gui;

import xyz.theasylum.zendarva.component.Inventory;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.drawable.widget.Widget;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class GuiInventory extends GuiWindow {
    private final Entity entity;

    private static Font font;
    int index = 0;
    Entity[] inventory = new Entity[10];

    @Override
    public void processKeystroke(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP:
                index--;
                if (index <0){
                    index = 0;
                }
                break;
            case KeyEvent.VK_DOWN:
                index++;
                if (index >10){
                    index = 10;
                }
                break;
        }
    }

    public GuiInventory(int width, int height, Entity entity) {
        super(width, height);
        this.entity = entity;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, Widget.class.getResourceAsStream("/square.ttf"));
            font=font.deriveFont(12F);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Optional<Inventory> i = entity.getComponent(Inventory.class);

        if (!i.isPresent())
            GuiManager.instance().removeWindow(this);

        Inventory inv = i.get();
        int count = 0;
        for (Entity entity1 : inv.getItems()) {
            inventory[count]=entity1;
            count++;
        }


    }

    @Override
    public void drawBackground(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(0,0,width,height);
        g.setColor(Color.green);
        g.fillRect(10,index*16+1,width-20,16);
    }

    @Override
    public void drawForeground(Graphics g) {

        g.setColor(Color.white);


        int y = 16;
        int x = 10;
        g.setFont(font);
        int yOffset = g.getFontMetrics().getHeight() + 3;
        for (int i = 0; i < 10; i++) {
            if (inventory[i] != null)
                g.drawString(inventory[i].name,x,y);
            y+=yOffset;
        }

    }




}
