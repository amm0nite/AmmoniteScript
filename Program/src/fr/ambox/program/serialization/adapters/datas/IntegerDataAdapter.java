package fr.ambox.program.serialization.adapters.datas;

import com.google.gson.*;
import fr.ambox.assembler.datas.IntegerData;

import java.lang.reflect.Type;

public class IntegerDataAdapter implements JsonDeserializer<IntegerData>, JsonSerializer<IntegerData> {
    @Override
    public IntegerData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new IntegerData(jsonElement.getAsLong());
    }

    @Override
    public JsonElement serialize(IntegerData integerData, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(integerData.longValue());
    }
}
