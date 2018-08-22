package fr.ambox.program.serialization.adapters.datas;

import com.google.gson.*;
import fr.ambox.assembler.datas.DecimalData;

import java.lang.reflect.Type;

public class DecimalDataAdapter implements JsonDeserializer<DecimalData>, JsonSerializer<DecimalData> {
    @Override
    public DecimalData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new DecimalData(jsonElement.getAsDouble());
    }

    @Override
    public JsonElement serialize(DecimalData decimalData, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(decimalData.doubleValue());
    }
}
