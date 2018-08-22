package fr.ambox.lexer.tokenClasses;

import java.util.ArrayList;

import fr.ambox.lexer.TokenClass;
import fr.ambox.lexer.TokenClassName;

public class StringAutomata implements TokenClass {

	private ArrayList<Character> chars;
	private Character delimiter;
	private StringAutomataState state;
	
	public StringAutomata() {
		this.reset();
	}
	
	public void reset() {
		this.state = StringAutomataState.Init;
		this.chars = new ArrayList<Character>();
		this.delimiter = null;
	}
	
	@Override
	public boolean isAvailable() {
		return this.state != StringAutomataState.Fail;
	}

	@Override
	public boolean isComplete() {
		return this.state == StringAutomataState.End;
	}
	
	public void feed(char c) {
		this.chars.add(c);
		this.state = this.update(c);
	}
	
	private StringAutomataState update(char c) {
		if (this.state == StringAutomataState.Init) {
			if (c == '"') {
				this.delimiter = c;
				return StringAutomataState.Start;
			}
			if (c == '\'') {
				this.delimiter = c;
				return StringAutomataState.Start;
			}
			return StringAutomataState.Fail;
		}
		
		if (this.state == StringAutomataState.Start) {
			if (c == this.delimiter) {
				return StringAutomataState.End;
			}
			if (c == '\\') {
				return StringAutomataState.Ignore;
			}
			return StringAutomataState.Start;
		}
		
		if (this.state == StringAutomataState.Ignore) {
			return StringAutomataState.Start;
		}
		
		return StringAutomataState.Fail;
	}
	
	@Override
	public TokenClassName getName() {
		return TokenClassName.String;
	}

	@Override
	public String getBuffer() {
		StringBuilder sb = new StringBuilder();
		for (char c: this.chars) {
			sb.append(c);
		}
		return sb.toString();
	}
}
