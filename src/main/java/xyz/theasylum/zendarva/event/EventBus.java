package xyz.theasylum.zendarva.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class EventBus {

    private static EventBus myInstance;

    private List<Object> eventHandlers;

    public static EventBus instance(){
        if (myInstance == null) {
            myInstance=new EventBus();
        }
        return myInstance;
    }

    private EventBus(){
        eventHandlers=new LinkedList<>();
    }

    public void registerHandler(Object eventHandler) {
        eventHandlers.add(eventHandler);
    }

    public void unregisterHandler(Object eventHandler) {
        eventHandlers.remove(eventHandler);
    }

    public void raiseEvent(Event event){
        for (Object obj : eventHandlers) {
            for (Method declaredMethod : obj.getClass().getDeclaredMethods()) {
                if (declaredMethod.getParameterCount() != 1)
                    continue;
                if (event.getClass().isAssignableFrom(declaredMethod.getParameters()[0].getType())){
                    try {
                        declaredMethod.setAccessible(true);
                        declaredMethod.invoke(obj,event);
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
