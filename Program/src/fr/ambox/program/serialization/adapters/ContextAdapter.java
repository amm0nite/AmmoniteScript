package fr.ambox.program.serialization.adapters;

import com.google.gson.*;
import fr.ambox.assembler.*;
import fr.ambox.program.serialization.*;

import java.lang.reflect.Type;
import java.util.ArrayDeque;

public class ContextAdapter implements JsonSerializer<Context>, JsonDeserializer<Context> {
    private Lexicon lexicon;

    public ContextAdapter() {
        this.lexicon = new Lexicon();
    }

    private Gson buildGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Step.class, new StepAdapter());
        gsonBuilder.registerTypeAdapter(Lexicon.class, new LexiconAdapter());
        gsonBuilder.registerTypeAdapter(AtomList.class, new AtomListAdapter());
        gsonBuilder.registerTypeAdapter(Data.class, new DataAdapter(this.lexicon));
        gsonBuilder.registerTypeAdapter(Stack.class, new StackAdapter(this.lexicon));
        gsonBuilder.registerTypeAdapter(DataStore.class, new DataStoreAdapter(this.lexicon));
        gsonBuilder.registerTypeAdapter(DictStack.class, new DictStackAdapter(this.lexicon));
        return gsonBuilder.create();
    }

    @Override
    public Context deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Gson gson = this.buildGson();

        Context context = new Context();
        JsonObject contextJo = jsonElement.getAsJsonObject();

        // lexicon
        this.lexicon.extend(gson.fromJson(contextJo.getAsJsonArray("lexicon"), Lexicon.class));

        // bootstrap
        context.setBootstrap(this.deserializeBootstrap(gson, contextJo.getAsJsonArray("bootstrap")));

        // store
        context.setStore(gson.fromJson(contextJo.get("store"), DataStore.class));

        // skipState
        context.setSkipState(SkipState.valueOf(contextJo.get("skipState").getAsString()));

        // stopFlag
        context.setStopFlag(contextJo.get("stopFlag").getAsBoolean());

        // stack
        context.setStack(gson.fromJson(contextJo.get("stack"), Stack.class));

        // dictStack
        context.setDictStack(gson.fromJson(contextJo.get("dictStack"), DictStack.class));

        // savedStacks
        context.setSavedStacks(this.deserializeSavedStacks(gson, contextJo.getAsJsonArray("savedStacks")));

        // blockBuffer
        context.setBlockBuffer(gson.fromJson(contextJo.get("blockBuffer"), AtomList.class));

        // blockCounter
        context.setBlockCounter(contextJo.get("blockCounter").getAsInt());

        // blockFlag
        context.setBlockFlag(contextJo.get("blockFlag").getAsBoolean());

        // stepStack
        context.setStepStack(this.deserializeStepStack(gson, contextJo.getAsJsonArray("stepStack")));

        // callStack
        context.setCallStack(this.deserializeCallStack(gson, contextJo.getAsJsonArray("callStack")));

        // metadata
        context.setMeta(gson.fromJson(contextJo.get("meta"), MetaData.class));

        return context;
    }

    @Override
    public JsonElement serialize(Context context, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = this.buildGson();

        JsonObject contextJo = new JsonObject();

        // bootstrap
        contextJo.add("bootstrap", this.serializeBootstrap(gson, context.getBootstrap()));

        // store
        contextJo.add("store", gson.toJsonTree(context.getStore(), DataStore.class));

        // skipState
        contextJo.addProperty("skipState", context.getSkipState().name());

        // stopFlag
        contextJo.addProperty("stopFlag", context.getStopFlag());

        // stack
        contextJo.add("stack", gson.toJsonTree(context.getStack(), Stack.class));

        // dictStack
        contextJo.add("dictStack", gson.toJsonTree(context.getDictStack(), DictStack.class));

        // savedStacks
        contextJo.add("savedStacks", this.serializeSavedStacks(gson, context.getSavedStacks()));

        // blockBuffer
        contextJo.add("blockBuffer", gson.toJsonTree(context.getBlockBuffer(), AtomList.class));

        // blockCounter
        contextJo.addProperty("blockCounter", context.getBlockCounter());

        // blockFlag
        contextJo.addProperty("blockFlag", context.getBlockFlag());

        // stepStack
        contextJo.add("stepStack", this.serializeStepStack(gson, context.getStepStack()));

        // callStack
        contextJo.add("callStack", this.serializeCallStack(gson, context.getCallStack()));

        // metadata
        contextJo.add("meta", gson.toJsonTree(context.getMeta(), MetaData.class));

        // lexicon
        contextJo.add("lexicon", gson.toJsonTree(this.lexicon, Lexicon.class));

        return contextJo;
    }

    private ArrayDeque<Step> deserializeBootstrap(Gson gson, JsonArray bootstrapJa) {
        ArrayDeque<Step> bootstrap = new ArrayDeque<Step>();

        for (JsonElement element: bootstrapJa) {
            JsonObject stepJo = element.getAsJsonObject();
            Step step = gson.fromJson(stepJo, Step.class);
            bootstrap.add(step);
        }

        return bootstrap;
    }

    private JsonElement serializeBootstrap(Gson gson, ArrayDeque<Step> bootstrap) {
        JsonArray bootstrapJa = new JsonArray();
        for (Step step: bootstrap) {
            bootstrapJa.add(gson.toJsonTree(step));
        }
        return bootstrapJa;
    }

    private ArrayDeque<Data[]> deserializeSavedStacks(Gson gson, JsonArray savedStacksJa) {
        ArrayDeque<Data[]> savedStacks = new ArrayDeque<Data[]>();
        for (JsonElement stackElement: savedStacksJa) {
            JsonArray stackJa = stackElement.getAsJsonArray();
            Data[] stack = new Data[stackJa.size()];
            for (int i=0; i<stack.length; i++) {
                JsonElement dataJe = stackJa.get(i);
                stack[i] = gson.fromJson(dataJe, Data.class);
            }
            savedStacks.add(stack);
        }
        return savedStacks;
    }

    private JsonElement serializeSavedStacks(Gson gson, ArrayDeque<Data[]> savedStacks) {
        JsonArray savedStacksJa = new JsonArray();
        for (Data[] stack: savedStacks) {
            JsonArray stackJa = new JsonArray();
            for (Data data: stack) {
                stackJa.add(gson.toJsonTree(data, Data.class));
            }
            savedStacksJa.add(stackJa);
        }
        return savedStacksJa;
    }

    private ArrayDeque<Step> deserializeStepStack(Gson gson, JsonArray stepStackJa) {
        ArrayDeque<Step> stepStack = new ArrayDeque<Step>();
        for (JsonElement stepJe: stepStackJa) {
            stepStack.add(gson.fromJson(stepJe, Step.class));
        }
        return stepStack;
    }

    private JsonElement serializeStepStack(Gson gson, ArrayDeque<Step> stepStack) {
        JsonArray stepStackJa = new JsonArray();
        for (Step step: stepStack) {
            stepStackJa.add(gson.toJsonTree(step, Step.class));
        }
        return stepStackJa;
    }

    private ArrayDeque<Trace> deserializeCallStack(Gson gson, JsonArray callStackJa) {
        ArrayDeque<Trace> callStack = new ArrayDeque<Trace>();
        for (JsonElement traceJe: callStackJa) {
            callStack.add(gson.fromJson(traceJe, Trace.class));
        }
        return callStack;
    }

    private JsonElement serializeCallStack(Gson gson, ArrayDeque<Trace> callStack) {
        JsonArray callStackJa = new JsonArray();
        for (Trace trace: callStack) {
            callStackJa.add(gson.toJsonTree(trace, Trace.class));
        }
        return callStackJa;
    }
}
