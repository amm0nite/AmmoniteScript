package fr.ambox.program.serialization.adapters;

import com.google.gson.*;
import fr.ambox.assembler.Atom;
import fr.ambox.assembler.atoms.*;
import fr.ambox.lexer.TokenPosition;

import java.lang.reflect.Type;

public class AtomAdapter implements JsonDeserializer<Atom>, JsonSerializer<Atom> {
    @Override
    public Atom deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Gson gson = new Gson();

        JsonObject atomJo = jsonElement.getAsJsonObject();
        String atomType = atomJo.get("type").getAsString();
        String token = atomJo.get("token").getAsString();

        Atom atom = this.make(atomType, token);
        TokenPosition position = gson.fromJson(atomJo.get("position"), TokenPosition.class);
        atom.setTokenPosition(position);
        return atom;
    }

    private Atom make(String atomType, String token) {
        switch (atomType) {
            case "CallAtom": return new CallAtom(token);
            case "DecimalAtom": return new DecimalAtom(token);
            case "InformationAtom": return new InformationAtom(token);
            case "IntegerAtom": return new IntegerAtom(token);
            case "NameAtom": return new NameAtom(token);
            case "NoopAtom": return new NoopAtom();
            case "ReferenceAtom": return new ReferenceAtom(token);
            case "StringAtom": return new StringAtom(token);
            case "VariableAtom": return new VariableAtom(token);
        }
        throw new JsonParseException("Unknown atom type (" + atomType + ")");
    }

    @Override
    public JsonElement serialize(Atom atom, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = new Gson();

        JsonObject atomJo = new JsonObject();
        atomJo.addProperty("type", atom.getClass().getSimpleName());
        atomJo.addProperty("token", atom.toTokenString());
        atomJo.add("position", gson.toJsonTree(atom.getTokenPosition()));
        return atomJo;
    }
}
