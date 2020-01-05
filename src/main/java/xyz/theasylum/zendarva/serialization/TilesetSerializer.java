package xyz.theasylum.zendarva.serialization;

import com.google.gson.*;
import xyz.theasylum.zendarva.Tileset;

import java.lang.reflect.Type;

public class TilesetSerializer implements JsonSerializer<Tileset>, JsonDeserializer<Tileset> {
    @Override
    public JsonElement serialize(Tileset src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject tilesetData = new JsonObject();
        tilesetData.addProperty("filename",src.filename);
        tilesetData.addProperty("tileWidth",src.tileWidth);
        tilesetData.addProperty("tileHeight",src.tileHeight);
        return tilesetData;
    }

    @Override
    public Tileset deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        System.out.println("Test");
        JsonObject jObj = json.getAsJsonObject();
        Tileset tileset = null;
        try {
            tileset = new Tileset(jObj.get("filename").getAsString());
        }
        catch (Exception e){
            tileset = new Tileset(jObj.get("filename").getAsString(), jObj.get("tileWidth").getAsInt(), jObj.get("tileHeight").getAsInt());
        }

        return tileset;
    }
}
