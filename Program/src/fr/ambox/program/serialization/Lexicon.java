package fr.ambox.program.serialization;

import com.google.gson.JsonParseException;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Store;

public class Lexicon extends Store<Data> {
    public Lexicon() {
        super();
    }

    public Data get(int id) {
        Data data = super.get(id);
        if (data == null) {
            throw new NotFoundInLexiconException("Not found in lexicon (" + id + ")");
        }
        return data;
    }

    public int add(Data data) {
        if (data == null) {
            throw new JsonParseException("Null not accepted in lexicon");
        }
        return super.add(data);
    }
}
