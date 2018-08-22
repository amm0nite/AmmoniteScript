package fr.ambox.lexer;

import java.util.ArrayList;
import java.util.Iterator;

abstract public class TokenClassList implements Iterable<TokenClass> {
	
	private ArrayList<TokenClass> classes;
	
	public TokenClassList() {
		this.classes = new ArrayList<TokenClass>();
	}

	public void addTokenClass(TokenClass tokenClass) {
		this.classes.add(tokenClass);
	}

	@Override
	public Iterator<TokenClass> iterator() {
		return this.classes.iterator();
	}

	public void reset() {
		for (TokenClass tc: this.classes) {
			tc.reset();
		}
	}

	public Token feed(char c) {
		for (TokenClass tc: this.classes) {
			if (tc.isAvailable()) {
				tc.feed(c);
			}
		}
		
		for (TokenClass tc: this.classes) {
			if (tc.isComplete()) {
				return new Token(tc, tc.getBuffer());
			}
		}
		
		return null;
	}

	public boolean oneAvailable() {
		for (TokenClass tc: this.classes) {
			if (tc.isAvailable()) {
				return true;
			}
		}
		return false;
	}

}
