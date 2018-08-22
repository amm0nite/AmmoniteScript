package fr.ambox.program.serialization.adapters.datas;

import com.google.gson.*;
import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.datas.BlockData;
import fr.ambox.program.serialization.adapters.AtomListAdapter;

import java.lang.reflect.Type;

public class BlockDataAdapter implements JsonDeserializer<BlockData>, JsonSerializer<BlockData> {
    private Gson buildGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AtomList.class, new AtomListAdapter());
        return gsonBuilder.create();
    }

    @Override
    public BlockData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Gson gson = this.buildGson();
        AtomList atoms = gson.fromJson(jsonElement, AtomList.class);
        return new BlockData(atoms);
    }

    @Override
    public JsonElement serialize(BlockData blockData, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = this.buildGson();
        AtomList atoms = blockData.getAtoms();
        return gson.toJsonTree(atoms, AtomList.class);
    }
}
