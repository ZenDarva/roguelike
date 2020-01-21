package xyz.theasylum.zendarva.event;

public class EventAddMessage extends Event {

    private String message;

    public EventAddMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
