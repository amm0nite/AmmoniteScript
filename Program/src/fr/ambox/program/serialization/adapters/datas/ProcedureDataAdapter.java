package fr.ambox.program.serialization.adapters.datas;

import com.google.gson.*;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.ProcedureData;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class ProcedureDataAdapter implements JsonSerializer<ProcedureData>, JsonDeserializer<ProcedureData> {

    private Procedure makeProcedure(String procedureName) {
        String className = "fr.ambox.assembler.procedures." + procedureName;
        try {
            Class<?> procedureClass = Class.forName(className);
            Constructor<?> constructor = procedureClass.getConstructor();
            Object thing = constructor.newInstance();
            if (!(thing instanceof Procedure)) {
                throw new JsonParseException("Not a procedure");
            }
            return (Procedure) thing;
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Class not found");
        } catch (NoSuchMethodException e) {
            throw new JsonParseException("No such method");
        } catch (IllegalAccessException e) {
            throw new JsonParseException("Illegal access");
        } catch (InstantiationException e) {
            throw new JsonParseException("Instantiation");
        } catch (InvocationTargetException e) {
            throw new JsonParseException("Invocation target");
        }
    }

    @Override
    public ProcedureData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String procedureName = jsonElement.getAsString();
        return new ProcedureData(this.makeProcedure(procedureName));
    }

    @Override
    public JsonElement serialize(ProcedureData procedureData, Type type, JsonSerializationContext jsonSerializationContext) {
        String procedureName = procedureData.getProcedure().getClass().getSimpleName();
        return new JsonPrimitive(procedureName);
    }
}
