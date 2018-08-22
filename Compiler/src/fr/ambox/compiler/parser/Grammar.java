package fr.ambox.compiler.parser;

public interface Grammar {
	NonTerminal getStart();
	NonTerminal nt(String ntName);
}
