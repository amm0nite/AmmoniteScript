package fr.ambox.assembler;

import fr.ambox.assembler.datas.IntegerData;
import fr.ambox.assembler.datas.StringData;
import fr.ambox.lexer.TokenPosition;

public class Trace {
    private TokenPosition tokenPosition;
    private int line;
    private String functionName;

    public Trace(TokenPosition position, Data lineData, Data functionNameData) {
        this.tokenPosition = new TokenPosition(-1, -1);
        if (position != null) {
            this.tokenPosition = position;
        }
        this.line = -1;
        if (lineData != null && lineData instanceof IntegerData) {
            this.line = ((IntegerData) lineData).intValue();
        }
        this.functionName = "internal";
        if (functionNameData != null && functionNameData instanceof StringData) {
            this.functionName = ((StringData) functionNameData).stringValue();
        }
    }

    public Trace(String name, Data lineData) {
        this(null, lineData, new StringData(name));
    }

    public String toString() {
        return this.functionName + " > " + this.tokenPosition + " > " + this.line;
    }

    public int getLine() {
        return this.line;
    }

    public TokenPosition getTokenPosition() {
        return this.tokenPosition;
    }

    public String getFunctionName() {
        return this.functionName;
    }
}
