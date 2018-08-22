package fr.ambox.program.serialization.adapters;

import com.google.gson.*;
import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.Step;
import fr.ambox.program.serialization.adapters.AtomListAdapter;

import java.lang.reflect.Type;

public class StepAdapter implements JsonSerializer<Step>, JsonDeserializer<Step> {
    @Override
    public Step deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AtomList.class, new AtomListAdapter());
        Gson gson = gsonBuilder.create();

        JsonObject stepJo = jsonElement.getAsJsonObject();
        AtomList atoms = gson.fromJson(stepJo.get("atoms"), AtomList.class);
        int offset = stepJo.get("offset").getAsInt();

        return new Step(atoms, offset);
    }

    @Override
    public JsonElement serialize(Step step, Type type, JsonSerializationContext jsonSerializationContext) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(AtomList.class, new AtomListAdapter());
        Gson gson = gsonBuilder.create();

        JsonObject stepJo = new JsonObject();
        stepJo.addProperty("offset", step.getOffset());
        stepJo.add("atoms", gson.toJsonTree(step.getAtoms()));
        return stepJo;
    }
}
