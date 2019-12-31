package xyz.theasylum.zendarva.gui.guiEvent;

public abstract class GuiEvent {

    protected boolean consumed = false;

    public void consume(){
        consumed = true;
    }
}
