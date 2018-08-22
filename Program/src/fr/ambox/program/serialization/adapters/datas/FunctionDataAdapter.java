package fr.ambox.program.serialization.adapters.datas;

import com.google.gson.*;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.datas.ArrayData;
import fr.ambox.assembler.datas.BlockData;
import fr.ambox.assembler.datas.FunctionData;
import fr.ambox.assembler.datas.TableData;
import fr.ambox.program.serialization.Lexicon;
import fr.ambox.program.serialization.NotFoundInLexiconException;

import java.lang.reflect.Type;

public class FunctionDataAdapter implements JsonDeserializer<FunctionData>, JsonSerializer<FunctionData> {
    private Lexicon lexicon;

    public FunctionDataAdapter(Lexicon lexicon) {
        this.lexicon = lexicon;
    }

    @Override
    public FunctionData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        FunctionData functionData = new FunctionData();

        try {
            JsonObject functionJo = jsonElement.getAsJsonObject();
            functionData.setName(functionJo.get("name").getAsString());

            Data argsData = this.lexicon.get(functionJo.get("args").getAsInt());
            if (!(argsData instanceof ArrayData)) {
                throw new JsonParseException("Expected ArrayData");
            }
            functionData.setArgs((ArrayData) argsData);

            Data codeData = this.lexicon.get(functionJo.get("code").getAsInt());
            if (!(codeData instanceof BlockData)) {
                throw new JsonParseException("Expected BlockData");
            }
            functionData.setCode((BlockData) codeData);

            Data localsData = this.lexicon.get(functionJo.get("locals").getAsInt());
            if (!(localsData instanceof TableData)) {
                throw new JsonParseException("Expected TableData");
            }
            functionData.setLocals((TableData) localsData);

            if (functionJo.has("parent")) {
                Data parentData = this.lexicon.get(functionJo.get("parent").getAsInt());
                functionData.setParent(parentData);
            }
        }
        catch (NotFoundInLexiconException e) {
            // data will be completed later at the end of the lexicon deserializer
            // only happens whe this is in the lexicon
        }

        return functionData;
    }

    @Override
    public JsonElement serialize(FunctionData functionData, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject functionJo = new JsonObject();
        functionJo.addProperty("name", functionData.getName());

        int argsId = this.lexicon.add(functionData.getArgs());
        functionJo.addProperty("args", argsId);

        int codeId = this.lexicon.add(functionData.getCode());
        functionJo.addProperty("code", codeId);

        int localsId = this.lexicon.add(functionData.getLocals());
        functionJo.addProperty("locals", localsId);

        Data parent = functionData.getParent();
        if (parent != null) {
            int parentId = this.lexicon.add(functionData.getParent());
            functionJo.addProperty("parent", parentId);
        }
        return functionJo;
    }
}
