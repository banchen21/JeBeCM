package org.bc.jeBeCM;

import com.google.gson.*;
import org.bukkit.Material;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

class MaterialMapDeserializer implements JsonDeserializer<Map<Material, CM_Item>> {
    @Override
    public Map<Material, CM_Item> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<Material, CM_Item> map = new HashMap<>();
        JsonObject jsonObject = json.getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            Material material = Material.getMaterial(entry.getKey());
            if (material != null) {
                CM_Item item = context.deserialize(entry.getValue(), CM_Item.class);
                map.put(material, item);
            } else {
                throw new JsonParseException("Invalid Material: " + entry.getKey());
            }
        }
        return map;
    }
}