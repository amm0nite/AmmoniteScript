package fr.ambox.lexer.tokenClasses;

import java.util.ArrayList;

import fr.ambox.lexer.TokenClass;
import fr.ambox.lexer.TokenClassName;

public class SpaceAutomata implements TokenClass {
	
	private ArrayList<Character> chars;
	private SpaceAutomataState state;
	
	public SpaceAutomata() {
		this.reset();
	}
	
	public void reset() {
		this.state = SpaceAutomataState.Init;
		this.chars = new ArrayList<Character>();
	}
	
	@Override
	public boolean isAvailable() {
		return this.state != SpaceAutomataState.Fail;
	}

	@Override
	public boolean isComplete() {
		return this.state == SpaceAutomataState.Space;
	}

	@Override
	public void feed(char c) {
		this.chars.add(c);
		this.state = this.update(c);
	}

	private SpaceAutomataState update(char c) {
		if (this.state == SpaceAutomataState.Init || this.state == SpaceAutomataState.Space) {
			if (c == ' ') {
				return SpaceAutomataState.Space;
			}
			if (c == '\t') {
				return SpaceAutomataState.Space;
			}
			if (c == '\r') {
				return SpaceAutomataState.Space;
			}
			if (c == '\n') {
				return SpaceAutomataState.Space;
			}
			return SpaceAutomataState.Fail;
		}
		return SpaceAutomataState.Fail;
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
		return TokenClassName.WhiteSpace;
	}
}
