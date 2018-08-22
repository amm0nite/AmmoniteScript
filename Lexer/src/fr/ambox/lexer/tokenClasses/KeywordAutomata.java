package fr.ambox.lexer.tokenClasses;

import java.util.ArrayList;

import fr.ambox.lexer.TokenClass;
import fr.ambox.lexer.TokenClassName;

public class KeywordAutomata implements TokenClass {
	
	private String keyword;
	private TokenClassName name;
	
	private int index;
	private ArrayList<Character> chars;
	private KeywordAutomataState state;
	
	
	public KeywordAutomata(String keyword, TokenClassName name) {
		this.keyword = keyword;
		this.name = name;
		this.reset();
	}
	
	@Override
	public void reset() {
		this.state = KeywordAutomataState.Init;
		this.chars = new ArrayList<Character>();
		this.index = 0;
	}
	
	@Override
	public boolean isAvailable() {
		return this.state != KeywordAutomataState.Fail;
	}

	@Override
	public boolean isComplete() {
		return this.state == KeywordAutomataState.End;
	}
	
	@Override
	public void feed(char c) {
		this.chars.add(c);
		this.state = this.update(c);
	}

	private KeywordAutomataState update(char c) {
		if (this.state == KeywordAutomataState.Init) {
			if (c == this.keyword.charAt(this.index)) {
				this.index++;
				if (this.index == this.keyword.length()) {
					return KeywordAutomataState.End;
				}
				return KeywordAutomataState.Init;
			}
			return KeywordAutomataState.Fail;
		}
		
		return KeywordAutomataState.Fail;
	}
	
	@Override
	public String getBuffer() {
		StringBuilder sb = new StringBuilder();
		for (char c: this.chars) {
			sb.append(c);
		}
		return sb.toString();
	}
	
	@Override
	public TokenClassName getName() {
		return this.name;
	}
}
