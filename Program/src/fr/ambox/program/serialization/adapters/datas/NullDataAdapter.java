package fr.ambox.program.serialization.adapters.datas;

import com.google.gson.*;
import fr.ambox.assembler.datas.NullData;

import java.lang.reflect.Type;

public class NullDataAdapter implements JsonDeserializer<NullData>, JsonSerializer<NullData> {
    @Override
    public NullData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new NullData();
    }

    @Override
    public JsonElement serialize(NullData nullData, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonObject();
    }
}
