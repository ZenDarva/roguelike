package xyz.theasylum.zendarva.gui.guiEvent;

import xyz.theasylum.zendarva.event.Event;
import xyz.theasylum.zendarva.gui.GuiWindow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class GuiEventBus {

    private static GuiEventBus myInstance;

    private List<Object> eventHandlers;

    public static GuiEventBus instance(){
        if (myInstance == null) {
            myInstance=new GuiEventBus();
        }
        return myInstance;
    }

    private GuiEventBus(){
        eventHandlers=new LinkedList<>();
    }

    public void registerHandler(Object eventHandler) {
        eventHandlers.add(eventHandler);
    }

    public void unregisterHandler(Object eventHandler) {
        eventHandlers.remove(eventHandler);
    }

    public void raiseEvent(GuiEvent event){
        for (Object obj : eventHandlers) {
            for (Method declaredMethod : obj.getClass().getDeclaredMethods()) {
                if (declaredMethod.getParameterCount() != 1)
                    continue;
                if (event.getClass().isAssignableFrom(declaredMethod.getParameters()[0].getType())){
                    try {
                        declaredMethod.invoke(obj,event);
                        break;
                    } catch (IllegalAccessException e) {
                    } catch (InvocationTargetException e) {
                    }
                }

            }
            if (event.consumed)
                break;
        }


    }

}
