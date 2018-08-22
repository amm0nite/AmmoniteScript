package fr.ambox.lexer;

import java.util.ArrayList;
import java.util.Iterator;

public class TokenList implements Iterable<Token> {
	private ArrayList<Token> list;
	
	public TokenList() {
		this.list = new ArrayList<Token>();
	}
	
	public TokenList(TokenList tokens) {
		this();
		for (Token t: tokens) {
			this.list.add(t);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Token t: this.list) {
			sb.append(t.toString());
			sb.append('\n');
		}
		return sb.toString();
	}

	public void add(Token t) {
		this.list.add(t);
	}

	@Override
	public Iterator<Token> iterator() {
		return this.list.iterator();
	}

	public Token get(int index) {
		return this.list.get(index);
	}

	public void removeLast() {
		this.list.remove(this.list.size() - 1);
	}

	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	public int size() {
		return this.list.size();
	}
}
