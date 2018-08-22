package fr.ambox.compiler.exceptions;

import fr.ambox.lexer.Token;
import fr.ambox.lexer.TokenClassName;

import java.util.ArrayList;

public class SyntaxErrorException extends Exception {

    private Token unexpectedToken;
    private Token afterToken;
    private ArrayList<TokenClassName> alternatives;

	public SyntaxErrorException(Token unexpectedToken, Token afterToken, ArrayList<TokenClassName> alternatives) {
        this.unexpectedToken = unexpectedToken;
        this.afterToken = afterToken;
        this.alternatives = alternatives;
	}

	@Override
	public String getMessage() {
        String message = "Syntax error: ";
        if (this.unexpectedToken != null) {
            message += "Unexpected " + this.unexpectedToken;
        }
        if (this.afterToken != null) {
            message += " after " + this.afterToken;
        }
        message += ", expecting: " + this.alternatives;
        return message;
	}

    public Token getUnexpectedToken() {
        return this.unexpectedToken;
    }

    public Token getAfterToken() {
        return this.afterToken;
    }

    public ArrayList<TokenClassName> getAlternatives() {
        return alternatives;
    }
}
