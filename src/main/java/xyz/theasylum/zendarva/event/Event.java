package xyz.theasylum.zendarva.event;

public abstract class Event {

    protected boolean consumed = false;

    public void consume(){
        consumed = true;
    }

}
