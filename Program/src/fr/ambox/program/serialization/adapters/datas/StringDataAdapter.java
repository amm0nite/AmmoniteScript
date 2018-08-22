package fr.ambox.program.serialization.adapters.datas;

import com.google.gson.*;
import fr.ambox.assembler.datas.StringData;

import java.lang.reflect.Type;

public class StringDataAdapter implements JsonDeserializer<StringData>, JsonSerializer<StringData> {

    @Override
    public StringData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new StringData(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(StringData stringData, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(stringData.stringValue());
    }
}
