package fr.ambox.program.serialization.adapters.datas;

import com.google.gson.*;
import fr.ambox.assembler.datas.NameData;

import java.lang.reflect.Type;

public class NameDataAdapter implements JsonDeserializer<NameData>, JsonSerializer<NameData> {
    @Override
    public NameData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new NameData(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(NameData nameData, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(nameData.getValue());
    }
}
