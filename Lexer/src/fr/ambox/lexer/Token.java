package fr.ambox.lexer;

public class Token {

	private TokenClass tokenClass;
	private String lexeme;
	private TokenPosition position;

	public Token(TokenClass tc, String lexeme) {
		this.tokenClass = tc;
		this.lexeme = lexeme;
	}

	public String getLexeme() {
		return this.lexeme;
	}

	public String toString() {
		return this.tokenClass.getName()+": '" + this.lexeme + "' (" + this.position + ")";
	}

	public TokenClass getTokenClass() {
		return this.tokenClass;
	}

	public TokenPosition getPosition() {
		return this.position;
	}
	
	public void setPosition(TokenPosition tokenPosition) {
		this.position = tokenPosition;
	}
}
