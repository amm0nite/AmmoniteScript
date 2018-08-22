package fr.ambox.program.serialization.adapters;

import com.google.gson.*;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.DictStack;
import fr.ambox.assembler.datas.ArrayData;
import fr.ambox.assembler.datas.FunctionData;
import fr.ambox.assembler.datas.TableData;
import fr.ambox.program.serialization.Lexicon;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class LexiconAdapter implements JsonDeserializer<Lexicon>, JsonSerializer<Lexicon> {

    @Override
    public Lexicon deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Lexicon lexicon = new Lexicon();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Data.class, new DataAdapter(lexicon));
        Gson gson = gsonBuilder.create();

        JsonArray lexiconJa = jsonElement.getAsJsonArray();
        HashMap<Integer, JsonElement> dataJeMap = new HashMap<Integer, JsonElement>();

        // create all references
        for (JsonElement lexictonJe: lexiconJa) {
            JsonObject lexictonJo = lexictonJe.getAsJsonObject();
            int id = lexictonJo.get("id").getAsInt();
            JsonElement dataJe = lexictonJo.get("data");
            dataJeMap.put(id, dataJe);
            Data data = gson.fromJson(lexictonJo.get("data"), Data.class);
            lexicon.put(id, data);
        }

        // complete the incomplete data
        for (Integer id: lexicon.keySet()) {
            Data data = lexicon.get(id);
            Data completeData = gson.fromJson(dataJeMap.get(id), Data.class);
            if (!data.isEqualTo(completeData)) {
                this.complete(data, completeData);
            }
        }

        return lexicon;
    }

    private void complete(Data incompleteData, Data completeData) {
        if (incompleteData instanceof TableData && completeData instanceof TableData) {
            TableData incompleteTable = (TableData) incompleteData;
            TableData completeTable = (TableData) completeData;
            incompleteTable.mimic(completeTable);
        }
        else if (incompleteData instanceof ArrayData && completeData instanceof ArrayData) {
            ArrayData incompleteArray = (ArrayData) incompleteData;
            ArrayData completeArray = (ArrayData) completeData;
            incompleteArray.mimic(completeArray);
        }
        else if (incompleteData instanceof FunctionData && completeData instanceof  FunctionData) {
            FunctionData incompleteFunction = (FunctionData) incompleteData;
            FunctionData completeFunction = (FunctionData) completeData;
            incompleteFunction.mimic(completeFunction);
        }
        else if (incompleteData instanceof DictStack && completeData instanceof DictStack) {
            DictStack incompleteDictStack = (DictStack) incompleteData;
            DictStack completeDictStack = (DictStack) completeData;
            incompleteDictStack.mimic(completeDictStack);
        }
        else {
            throw new JsonParseException("can not complete data");
        }
    }

    @Override
    public JsonElement serialize(Lexicon lexicon, Type type, JsonSerializationContext jsonSerializationContext) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Data.class, new DataAdapter(lexicon));
        Gson gson = gsonBuilder.create();

        JsonArray lexiconJa = new JsonArray();
        ArrayList<Integer> keys = new ArrayList<Integer>();
        ArrayList<Integer> dones = new ArrayList<Integer>();
        boolean done = false;

        while (!done) {
            int previousLexiconSize = lexicon.size();
            keys.clear();

            for (Integer key: lexicon.keySet()) {
                if (!dones.contains(key)) {
                    keys.add(key);
                }
            }

            for (Integer key: keys) {
                JsonObject lexictonJo = new JsonObject();
                lexictonJo.addProperty("id", key);
                Data data = lexicon.get(key);
                lexictonJo.add("data", gson.toJsonTree(data, Data.class));
                lexiconJa.add(lexictonJo);
                dones.add(key);
            }

            // done if nothing was added to the lexicon
            done = (lexicon.size() == previousLexiconSize);
        }

        return lexiconJa;
    }
}
