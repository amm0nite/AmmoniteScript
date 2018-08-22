package fr.ambox.program.serialization.adapters;

import com.google.gson.*;
import fr.ambox.assembler.Context;
import fr.ambox.program.Program;
import fr.ambox.program.ProgramState;
import fr.ambox.program.serialization.adapters.ContextAdapter;

import java.lang.reflect.Type;

public class ProgramAdapter implements JsonSerializer<Program>, JsonDeserializer<Program> {

    @Override
    public JsonElement serialize(Program program, Type type, JsonSerializationContext jsonSerializationContext) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Context.class, new ContextAdapter());
        Gson gson = gsonBuilder.create();

        JsonObject programJo = new JsonObject();
        programJo.addProperty("state", program.getState().name());
        programJo.add("context", gson.toJsonTree(program.getContext()));

        return programJo;
    }

    @Override
    public Program deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Context.class, new ContextAdapter());
        Gson gson = gsonBuilder.create();

        JsonObject programJo = jsonElement.getAsJsonObject();
        ProgramState state = ProgramState.valueOf(programJo.get("state").getAsString());
        Context context = gson.fromJson(programJo.get("context"), Context.class);

        return new Program(context, state);
    }
}
