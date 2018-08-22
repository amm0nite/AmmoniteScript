package fr.ambox.program.serialization.adapters;

import com.google.gson.*;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Stack;
import fr.ambox.program.serialization.Lexicon;

import java.lang.reflect.Type;
import java.util.HashMap;

public class StackAdapter implements JsonDeserializer<Stack>, JsonSerializer<Stack> {

    private Lexicon lexicon;

    public StackAdapter(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    @Override
    public Stack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Stack stack = new Stack();

        JsonArray stackJa = jsonElement.getAsJsonArray();
        for (JsonElement dataJe: stackJa) {
            int id = dataJe.getAsInt();
            Data data = this.lexicon.get(id);
            stack.add(data);
        }

        return stack;
    }

    @Override
    public JsonElement serialize(Stack stack, Type type, JsonSerializationContext jsonSerializationContext) {
        Data[] datas = stack.toArray();

        JsonArray stackJa = new JsonArray();
        for (Data data: datas) {
            int id = this.lexicon.add(data);
            stackJa.add(id);
        }

        return stackJa;
    }
}
