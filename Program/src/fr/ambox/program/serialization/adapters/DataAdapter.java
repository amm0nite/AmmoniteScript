package fr.ambox.program.serialization.adapters;

import com.google.gson.*;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.DictStack;
import fr.ambox.assembler.datas.*;
import fr.ambox.program.serialization.Lexicon;
import fr.ambox.program.serialization.adapters.datas.*;

import java.lang.reflect.Type;

public class DataAdapter implements JsonDeserializer<Data>, JsonSerializer<Data> {
    private Lexicon lexicon;

    public DataAdapter(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    private Gson buildGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(NullData.class, new NullDataAdapter());
        gsonBuilder.registerTypeAdapter(NameData.class, new NameDataAdapter());
        gsonBuilder.registerTypeAdapter(StateData.class, new StateDataAdapter());
        gsonBuilder.registerTypeAdapter(BlockData.class, new BlockDataAdapter());
        gsonBuilder.registerTypeAdapter(StringData.class, new StringDataAdapter());
        gsonBuilder.registerTypeAdapter(IntegerData.class, new IntegerDataAdapter());
        gsonBuilder.registerTypeAdapter(DecimalData.class, new DecimalDataAdapter());
        gsonBuilder.registerTypeAdapter(BooleanData.class, new BooleanDataAdapter());
        gsonBuilder.registerTypeAdapter(ProcedureData.class, new ProcedureDataAdapter());
        gsonBuilder.registerTypeAdapter(TableData.class, new TableDataAdapter(this.lexicon));
        gsonBuilder.registerTypeAdapter(ArrayData.class, new ArrayDataAdapter(this.lexicon));
        gsonBuilder.registerTypeAdapter(DictStack.class, new DictStackAdapter(this.lexicon));
        gsonBuilder.registerTypeAdapter(FunctionData.class, new FunctionDataAdapter(this.lexicon));
        return gsonBuilder.create();
    }

    @Override
    public Data deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Gson gson = this.buildGson();

        JsonObject dataJo = jsonElement.getAsJsonObject();
        String dataType = dataJo.get("type").getAsString();
        JsonElement contentJe = dataJo.get("content");

        // DictStack
        if (dataType.equals("DictStack")) {
            return gson.fromJson(contentJe, DictStack.class);
        }

        // All data
        try {
            String className = "fr.ambox.assembler.datas." + dataType;
            Class dataClass = Class.forName(className);
            Object thing = gson.fromJson(contentJe, dataClass);
            if (!(thing instanceof Data)) {
                throw new JsonParseException("Not parsed to Data");
            }
            return (Data) thing;
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Could not find data class (" + dataType + ")");
        }
    }

    @Override
    public JsonElement serialize(Data data, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = this.buildGson();

        JsonObject dataJo = new JsonObject();
        dataJo.addProperty("type", data.getClass().getSimpleName());
        dataJo.add("content", gson.toJsonTree(data, data.getClass()));

        return dataJo;
    }
}
