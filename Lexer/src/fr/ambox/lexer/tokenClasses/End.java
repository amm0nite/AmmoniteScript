package fr.ambox.lexer.tokenClasses;

import fr.ambox.lexer.TokenClass;
import fr.ambox.lexer.TokenClassName;

public class End implements TokenClass {

	@Override
	public TokenClassName getName() {
		return TokenClassName.End;
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void feed(char c) {
		
	}

	@Override
	public String getBuffer() {
		return null;
	}

	@Override
	public boolean isAvailable() {
		return false;
	}

	@Override
	public boolean isComplete() {
		return false;
	}
}
