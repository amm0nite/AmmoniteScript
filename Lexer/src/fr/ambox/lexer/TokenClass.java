package fr.ambox.lexer;

public interface TokenClass {
	void reset();
	void feed(char c);
	String getBuffer();
	TokenClassName getName();
	boolean isAvailable();
	boolean isComplete();
}
