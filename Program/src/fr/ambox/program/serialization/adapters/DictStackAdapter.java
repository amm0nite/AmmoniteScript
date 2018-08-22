package fr.ambox.program.serialization.adapters;

import com.google.gson.*;
import fr.ambox.assembler.DictStack;
import fr.ambox.assembler.datas.TableData;
import fr.ambox.program.serialization.Lexicon;
import fr.ambox.program.serialization.NotFoundInLexiconException;

import java.lang.reflect.Type;

public class DictStackAdapter implements JsonDeserializer<DictStack>, JsonSerializer<DictStack> {
    private Lexicon lexicon;

    public DictStackAdapter(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    @Override
    public DictStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        DictStack dictStack = new DictStack();

        try {
            JsonArray dictStackJa = jsonElement.getAsJsonArray();
            for (JsonElement dictJe : dictStackJa) {
                int id = dictJe.getAsInt();
                TableData dict = (TableData) this.lexicon.get(id);
                dictStack.add(dict);
            }
        }
        catch (NotFoundInLexiconException e) {
            // data will be completed later at the end of the lexicon deserializer
            // only happens whe this is in the lexicon
        }

        return dictStack;
    }

    @Override
    public JsonElement serialize(DictStack dictStack, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray dictStackJa = new JsonArray();

        for (TableData dict: dictStack) {
            int id = this.lexicon.add(dict);
            dictStackJa.add(id);
        }

        return dictStackJa;
    }
}
