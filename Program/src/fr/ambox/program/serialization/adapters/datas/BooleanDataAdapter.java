package fr.ambox.program.serialization.adapters.datas;

import com.google.gson.*;
import fr.ambox.assembler.datas.BooleanData;

import java.lang.reflect.Type;

public class BooleanDataAdapter implements JsonDeserializer<BooleanData>, JsonSerializer<BooleanData> {
    @Override
    public BooleanData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new BooleanData(jsonElement.getAsBoolean());
    }

    @Override
    public JsonElement serialize(BooleanData booleanData, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(booleanData.getValue());
    }
}
