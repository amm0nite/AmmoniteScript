package fr.ambox.program.serialization.adapters.datas;

import com.google.gson.*;
import fr.ambox.assembler.ProcedureState;
import fr.ambox.assembler.datas.StateData;

import java.lang.reflect.Type;

public class StateDataAdapter implements JsonDeserializer<StateData>, JsonSerializer<StateData> {
    @Override
    public StateData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new StateData(ProcedureState.valueOf(jsonElement.getAsString()));
    }

    @Override
    public JsonElement serialize(StateData stateData, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(stateData.getState().name());
    }
}
