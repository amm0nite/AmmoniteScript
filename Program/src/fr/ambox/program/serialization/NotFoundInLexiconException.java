package fr.ambox.program.serialization;

import com.google.gson.JsonParseException;

public class NotFoundInLexiconException extends JsonParseException {
    public NotFoundInLexiconException(String msg) {
        super(msg);
    }
}
