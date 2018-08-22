package fr.ambox.program.serialization.adapters.datas;

import com.google.gson.*;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.datas.ArrayData;
import fr.ambox.program.serialization.Lexicon;
import fr.ambox.program.serialization.NotFoundInLexiconException;

import java.lang.reflect.Type;

public class ArrayDataAdapter implements JsonDeserializer<ArrayData>, JsonSerializer<ArrayData> {

    private Lexicon lexicon;

    public ArrayDataAdapter(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    @Override
    public ArrayData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ArrayData arrayData = new ArrayData();

        try {
            JsonArray arrayDataJa = jsonElement.getAsJsonArray();
            for (JsonElement dataJe: arrayDataJa) {
                int id = dataJe.getAsInt();
                arrayData.append(this.lexicon.get(id));
            }
        }
        catch (NotFoundInLexiconException e) {
            // data will be completed later at the end of the lexicon deserializer
            // only happens whe this is in the lexicon
        }

        return arrayData;
    }

    @Override
    public JsonElement serialize(ArrayData arrayData, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray arrayDataJa = new JsonArray();
        for (Data data: arrayData) {
            int id = this.lexicon.add(data);
            arrayDataJa.add(id);
        }
        return arrayDataJa;
    }
}
