package fr.ambox.lexer.tokenClasses;

import java.util.ArrayList;

import fr.ambox.lexer.TokenClass;
import fr.ambox.lexer.TokenClassName;

public class OperatorsAutomata implements TokenClass {

	private ArrayList<Character> chars;
	private OperatorsAutomataState state;
	private String[] ops;
	private char first;
	private TokenClassName name;
	
	public OperatorsAutomata(String[] operators, TokenClassName name) {
		this.ops = operators;
		this.name = name;
		this.reset();
	}

	@Override
	public void reset() {
		this.first = '\u0000';
		this.state = OperatorsAutomataState.Init;
		this.chars = new ArrayList<Character>();
	}
	
	@Override
	public boolean isAvailable() {
		return this.state != OperatorsAutomataState.Fail;
	}
	
	@Override
	public boolean isComplete() {
		return this.state == OperatorsAutomataState.One || this.state == OperatorsAutomataState.Two;
	}
	
	@Override
	public void feed(char c) {
		this.chars.add(c);
		this.state = this.update(c);
	}

	private OperatorsAutomataState update(char c) {
		if (this.state == OperatorsAutomataState.Init) {
			for (String o: this.ops) {
				if (c == o.charAt(0)) {
					if (o.length() == 1) {
						this.first = c;
						return OperatorsAutomataState.One;
					}
					else if (o.length() == 2) {
						this.first = c;
						return OperatorsAutomataState.Wait;
					}
				}
			}
			return OperatorsAutomataState.Fail;
		}
		
		if (this.state == OperatorsAutomataState.One || this.state == OperatorsAutomataState.Wait) {
			for (String o: this.ops) {
				if (o.length() == 2) {
					if (this.first == o.charAt(0) && c == o.charAt(1)) {
						return OperatorsAutomataState.Two;
					}
				}
			}
			return OperatorsAutomataState.Fail;
		}
		
		return OperatorsAutomataState.Fail;
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
