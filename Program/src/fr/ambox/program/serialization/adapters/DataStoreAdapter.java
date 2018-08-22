package fr.ambox.program.serialization.adapters;

import com.google.gson.*;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.DataStore;
import fr.ambox.assembler.datas.IntegerData;
import fr.ambox.program.serialization.Lexicon;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DataStoreAdapter implements JsonSerializer<DataStore>, JsonDeserializer<DataStore> {
    private Lexicon lexicon;

    public DataStoreAdapter(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    @Override
    public DataStore deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        DataStore store = new DataStore();

        JsonArray storeJa = jsonElement.getAsJsonArray();
        for (JsonElement element: storeJa) {
            JsonObject elementJo = element.getAsJsonObject();
            int uniqueId = elementJo.get("data").getAsInt();
            Data data = this.lexicon.get(uniqueId);
            int id = store.add(data);
            if (id != elementJo.get("id").getAsInt()) {
                throw new JsonParseException("Store is shifted");
            }
        }

        return store;
    }

    @Override
    public JsonElement serialize(DataStore dataStore, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray storeJa = new JsonArray();

        for (Map.Entry<Integer, Data> elt: dataStore) {
            JsonObject entryJo = new JsonObject();
            entryJo.addProperty("id", elt.getKey());
            int uniqueId = this.lexicon.add(elt.getValue());
            entryJo.addProperty("data", uniqueId);
            storeJa.add(entryJo);
        }

        return storeJa;
    }
}
