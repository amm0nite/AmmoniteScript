package fr.ambox.lexer.tokenClasses;

import java.util.ArrayList;

import fr.ambox.lexer.TokenClass;
import fr.ambox.lexer.TokenClassName;

public class NumberAutomata implements TokenClass {

	private ArrayList<Character> chars;
	protected NumberAutomataState state;

	public NumberAutomata() {
		this.reset();
	}

	@Override
	public void reset() {
		this.state = NumberAutomataState.Init;
		this.chars = new ArrayList<Character>();
	}
	
	@Override
	public boolean isAvailable() {
		return this.state != NumberAutomataState.Fail;
	}
	
	@Override
	public boolean isComplete() {
		return this.state == NumberAutomataState.Integer || this.state == NumberAutomataState.Decimal;
	}
	
	@Override
	public void feed(char c) {
		this.chars.add(c);
		this.state = this.update(c);
	}

	private NumberAutomataState update(char c) {
		String s = "" + c;
		
		if (this.state == NumberAutomataState.Init) {
			if (c == '-') {
				return NumberAutomataState.Init;
			}
			if (s.matches("[0-9]")) {
				return NumberAutomataState.Integer;
			}
			return NumberAutomataState.Fail;
		}
		
		if (this.state == NumberAutomataState.Integer) {
			if (s.matches("[0-9]")) {
				return NumberAutomataState.Integer;
			}
			if (c == '.') {
				return NumberAutomataState.Dot;
			}
			return NumberAutomataState.Fail;
		}
		
		if (this.state == NumberAutomataState.Dot || this.state == NumberAutomataState.Decimal) {
			if (s.matches("[0-9]")) {
				return NumberAutomataState.Decimal;
			}
			return NumberAutomataState.Fail;
		}
		
		return NumberAutomataState.Fail;
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
		return TokenClassName.Number;
	}
}
