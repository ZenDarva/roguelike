package xyz.theasylum.zendarva.event;

import xyz.theasylum.zendarva.ITickable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class EventBus implements ITickable {

    private static EventBus myInstance;

    private List<Object> eventHandlers;

    private Queue<Event> events;

    public static EventBus instance(){
        if (myInstance == null) {
            myInstance=new EventBus();
        }
        return myInstance;
    }

    private EventBus(){
        eventHandlers=new LinkedList<>();
        events = new ArrayDeque<>();
    }

    public void registerHandler(Object eventHandler) {
        eventHandlers.add(eventHandler);
    }

    public void unregisterHandler(Object eventHandler) {
        eventHandlers.remove(eventHandler);
    }



    public void raiseEvent(Event event){
        events.add(event);
    }


    @Override
    public void update() {
        Event event;
        while( (event=events.poll())!=null)
            for (Object obj : eventHandlers) {
                for (Method declaredMethod : obj.getClass().getDeclaredMethods()) {
                    if (declaredMethod.getParameterCount() != 1)
                        continue;
                    if (event.getClass().isAssignableFrom(declaredMethod.getParameters()[0].getType())) {
                        try {
                            declaredMethod.setAccessible(true);
                            declaredMethod.invoke(obj, event);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

}
