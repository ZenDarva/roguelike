package xyz.theasylum.zendarva.serialization;

import com.google.gson.*;
import sun.misc.Unsafe;
import xyz.theasylum.zendarva.component.CombatStats;
import xyz.theasylum.zendarva.component.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class ComponentSerializer implements JsonSerializer<Component>, JsonDeserializer<Component> {
    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement element = context.serialize(src);
        element.getAsJsonObject().addProperty("Class", src.getClass().getCanonicalName());
        return element;
    }

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String className = obj.get("Class").getAsString();
        obj.remove("Class");
        Object classObj;
        try {
            Class clazz = this.getClass().getClassLoader().loadClass(className);
            return context.deserialize(json, clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
