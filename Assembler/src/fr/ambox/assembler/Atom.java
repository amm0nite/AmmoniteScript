package fr.ambox.assembler;

import fr.ambox.assembler.exceptions.FatalErrorException;
import fr.ambox.lexer.TokenPosition;

abstract public class Atom {
	private TokenPosition tokenPosition;

	abstract public boolean execute(Context context) throws FatalErrorException;
    abstract public String toTokenString();
    abstract public boolean isBlockBoundary();

	public void setTokenPosition(TokenPosition position) {
        this.tokenPosition = position;
    }

    public TokenPosition getTokenPosition() {
        return this.tokenPosition;
    }
}
