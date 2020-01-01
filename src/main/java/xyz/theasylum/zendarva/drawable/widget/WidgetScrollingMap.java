package xyz.theasylum.zendarva.drawable.widget;

import xyz.theasylum.zendarva.Game;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.domain.Floor;
import xyz.theasylum.zendarva.event.EventBus;
import xyz.theasylum.zendarva.event.EventEntity;
import xyz.theasylum.zendarva.gui.GuiWindow;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WidgetScrollingMap extends Widget {
    private final int width;
    private final int height;
    private final int scale;
    private Floor floor;
    private final BufferedImage texture;
    private Entity focusEntity;

    //Pixel size for width/height
    public WidgetScrollingMap(GuiWindow parent, int width, int height, int scale) {
        super(parent);
        this.width = width;
        this.height = height;
        this.scale = scale;

        EventBus.instance().registerHandler(this);
        texture = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
        focusEntity= Game.player;
    }
    public WidgetScrollingMap(GuiWindow parent, int width, int height) {
        this(parent,width,height,1);
    }


    @Override
    public void draw(Graphics g) {

        Graphics gl = texture.createGraphics();
        gl.setColor(Color.black);
        gl.fillRect(0,0,width,height);

        int tileWidth = floor.getTileset().tileWidth * scale;
        int tileHeight = floor.getTileset().tileHeight * scale;

        Point offset = getOffset();

        for (int x = offset.x; x< width/tileWidth+offset.x ;x++){
            for (int y = offset.y; y < height/tileHeight+offset.y ;y++){
                floor.getTileset().setTileNum(floor.getTiles()[x][y].tileNum);
                floor.getTileset().drawScaled(gl,(x-offset.x) *tileWidth,(y-offset.y)*tileHeight,scale);
            }
        }
        Rectangle rect = new Rectangle(offset.x,offset.y,width/tileWidth+offset.x,height/tileHeight+offset.x);
        for (Entity entity : floor.getEntities()) {
//            if (!rect.contains(entity.loc))
//                continue;
            floor.getTileset().setTileNum(entity.tileNum);
            floor.getTileset().drawScaled(gl,(entity.loc.x-offset.x)*tileWidth,(entity.loc.y -offset.y) *tileHeight,scale);
        }

        gl.dispose();
        g.drawImage(texture,loc.x,loc.y,null);
    }

    public void setFloor(Floor floor) {
        this.floor=floor;
    }

    @Override
    public void update() {
    }

//    private void damageEntity(EventEntity.EventDamageEntity e){
//        WidgetPopupText popup = new WidgetPopupText(parent,60, ""+e.getAmount() * -1);
//        popup.setLocation(new Point(e.getEntity().loc.x* floor.getTileset().tileWidth +8,e.getEntity().loc.y* floor.getTileset().tileHeight +8));
//        popup.visible=true;
//        parent.addWidget(popup);
//
//    }

    private Point getOffset(){
        int tileWidth = floor.getTileset().tileWidth * scale;
        int tileHeight = floor.getTileset().tileHeight * scale;

        int x = Math.max(0,focusEntity.loc.x-(width/tileWidth)/2);
        int y = Math.max(0,focusEntity.loc.y-(height/tileHeight)/2);
        x = Math.min((floor.getWidth()-width/tileWidth)-1,x);
        y = Math.min((floor.getHeight()-height/tileHeight)-1,y);
        return new Point(x,y);
    }

    public void setFocus(Entity focusEntity){

        this.focusEntity = focusEntity;
    }
}
