package fr.ambox.program.serialization.adapters.datas;

import com.google.gson.*;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.datas.TableData;
import fr.ambox.program.serialization.Lexicon;
import fr.ambox.program.serialization.NotFoundInLexiconException;

import java.lang.reflect.Type;
import java.util.Map;

public class TableDataAdapter implements JsonDeserializer<TableData>, JsonSerializer<TableData> {

    private Lexicon lexicon;

    public TableDataAdapter(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    @Override
    public TableData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        TableData tableData = new TableData();

        try {
            JsonObject tableJo = jsonElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry: tableJo.entrySet()) {
                int id = entry.getValue().getAsInt();
                Data data = this.lexicon.get(id);
                tableData.safePut(entry.getKey(), data);
            }
        }
        catch (NotFoundInLexiconException e) {
            // data will be completed later at the end of the lexicon deserializer
            // only happens whe this is in the lexicon
        }

        return tableData;
    }

    @Override
    public JsonElement serialize(TableData tableData, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject tableJo = new JsonObject();

        for (Map.Entry<String, Data> entry: tableData.entrySet()) {
            int id = this.lexicon.add(entry.getValue());
            tableJo.addProperty(entry.getKey(), id);
        }

        return tableJo;
    }
}
