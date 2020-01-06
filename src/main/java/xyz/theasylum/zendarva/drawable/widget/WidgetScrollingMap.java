package xyz.theasylum.zendarva.drawable.widget;

import xyz.theasylum.zendarva.Tileset;
import xyz.theasylum.zendarva.ai.map.BehaviorMapGenerator;
import xyz.theasylum.zendarva.component.Carryable;
import xyz.theasylum.zendarva.component.Renderable;
import xyz.theasylum.zendarva.domain.Entity;
import xyz.theasylum.zendarva.domain.Floor;
import xyz.theasylum.zendarva.domain.GameState;
import xyz.theasylum.zendarva.event.EventBus;
import xyz.theasylum.zendarva.event.EventEntity;
import xyz.theasylum.zendarva.gui.GuiWindow;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class WidgetScrollingMap extends Widget {
    private final int width;
    private final int height;
    private final BufferedImage texture;

    private Entity focusEntity;
    private int scale;
    int tileWidth;
    int tileHeight;

    //Pixel size for width/height
    public WidgetScrollingMap(GuiWindow parent, int width, int height, int scale) {
        super(parent);
        this.width = width;
        this.height = height;
        this.scale = scale;

        EventBus.instance().registerHandler(this);
        texture = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
        focusEntity= GameState.instance().player;

    }
    public WidgetScrollingMap(GuiWindow parent, int width, int height) {
        this(parent,width,height,1);
    }


    @Override
    public void draw(Graphics g) {
        Floor floor = GameState.instance().getCurFloor();

        Graphics gl = texture.createGraphics();
        gl.setColor(Color.black);
        gl.fillRect(0,0,width,height);

        Point offset = getOffset();

//        int[][] keyTargetMap = new BehaviorMapGenerator().generateFromEntities(floor, floor.getEntities().stream().filter(f->f.hasComponent(Carryable.class)).collect(Collectors.toList()));
        gl.setColor(Color.red);
        for (int x = offset.x; x< width/tileWidth+offset.x ;x++){
            for (int y = offset.y; y < height/tileHeight+offset.y ;y++){




                if (floor.getTiles()[x][y].visited == false){
  //                  gl.drawString(""+keyTargetMap[x][y], (x-offset.x)*tileWidth+tileWidth/2 ,(y-offset.y)*tileHeight+tileHeight/2);
                    continue;
                }
                floor.getTileset().setTileNum(floor.getTiles()[x][y].tileNum);
                floor.getTileset().drawScaled(gl,(x-offset.x) *tileWidth,(y-offset.y)*tileHeight,scale);
//                gl.drawString(""+keyTargetMap[x][y], (x-offset.x)*tileWidth +(tileWidth/2),(y-offset.y)*tileHeight +tileHeight/2);
            }
        }

        List<Point> playerFOV = floor.getFOV(GameState.instance().player);
        for (Entity entity : floor.getEntities()) {
            if (!playerFOV.contains(entity.loc))
                continue;
            if (floor.getTiles()[entity.loc.x][entity.loc.y].visited == false)
                continue;
            Optional<Renderable> entityRenderable = entity.getComponent(Renderable.class);
            entityRenderable.ifPresent(f->{
                Tileset set = GameState.instance().getTilest(f.getTilesetIndex());
                set.setTileNum(f.getTileNum());
                set.drawScaled(gl,worldToScreenCoords(entity.loc.x,entity.loc.y),scale);
            });
        }

        gl.dispose();
        g.drawImage(texture,loc.x,loc.y,null);
    }

    public void setFloor(Floor floor) {
        tileWidth = floor.getTileset().tileWidth * scale;
        tileHeight = floor.getTileset().tileHeight * scale;
    }

    public void setScale(int scale){
        this.scale=scale;
        Floor floor = GameState.instance().getCurFloor();
        tileWidth = floor.getTileset().tileWidth * scale;
        tileHeight = floor.getTileset().tileHeight * scale;
    }

    @Override
    public void update() {
        GameState.instance().getCurFloor().updateFOV(GameState.instance().player);
    }

    private void damageEntity(EventEntity.EventDamageEntity e){
        WidgetPopupText popup = new WidgetPopupText(parent,30, ""+e.getAmount() * -1);

        Point loc = worldToScreenCoords(e.getEntity().loc.x,e.getEntity().loc.y);

        loc.x+=tileWidth/2;
        loc.y+=tileHeight/2;
        popup.setLocation(loc);

        popup.visible=true;
        parent.addWidget(popup);

    }

    private Point worldToScreenCoords(int x, int y){
        Point point =getOffset();
        int lx = x*tileWidth-(point.x*tileWidth);
        int ly = y*tileHeight-(point.y*tileHeight);
        return new Point(lx,ly);
    }

    private Point getOffset(){
        Floor floor = GameState.instance().getCurFloor();
        Entity player = GameState.instance().player;
        int x = Math.max(0,player.loc.x-(width/tileWidth)/2);
        int y = Math.max(0,player.loc.y-(height/tileHeight)/2);
        x = Math.min((floor.getWidth()-width/tileWidth),x);
        y = Math.min((floor.getHeight()-height/tileHeight),y);
        return new Point(x,y);
    }

    public void setFocus(Entity focusEntity){
        this.focusEntity = focusEntity;
    }
}
