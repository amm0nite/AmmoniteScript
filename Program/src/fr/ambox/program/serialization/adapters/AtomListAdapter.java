package fr.ambox.program.serialization.adapters;

import com.google.gson.*;
import fr.ambox.assembler.Atom;
import fr.ambox.assembler.AtomList;
import fr.ambox.program.serialization.adapters.AtomAdapter;

import java.lang.reflect.Type;

public class AtomListAdapter implements JsonSerializer<AtomList>, JsonDeserializer<AtomList> {

    @Override
    public AtomList deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Atom.class, new AtomAdapter());
        Gson gson = gsonBuilder.create();

        JsonArray atomsJa = jsonElement.getAsJsonArray();
        AtomList atoms = new AtomList();
        for (JsonElement atomJe: atomsJa) {
            atoms.add(gson.fromJson(atomJe, Atom.class));
        }

        return atoms;
    }

    @Override
    public JsonElement serialize(AtomList atoms, Type type, JsonSerializationContext jsonSerializationContext) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Atom.class, new AtomAdapter());
        Gson gson = gsonBuilder.create();

        JsonArray atomListJa = new JsonArray();
        for (Atom atom: atoms) {
            atomListJa.add(gson.toJsonTree(atom, Atom.class));
        }

        return atomListJa;
    }
}
