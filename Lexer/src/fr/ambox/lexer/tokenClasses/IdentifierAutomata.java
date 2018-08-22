package fr.ambox.lexer.tokenClasses;

import java.util.ArrayList;

import fr.ambox.lexer.TokenClass;
import fr.ambox.lexer.TokenClassName;

public class IdentifierAutomata implements TokenClass {
	
	private ArrayList<Character> chars;
	private IdentifierAutomataState state;
	private TokenClassName tokenClassName;
	private Character prefix;

	public IdentifierAutomata() {
		this.reset();
		this.tokenClassName = TokenClassName.Identifier;
		this.prefix = null;
	}

	public IdentifierAutomata(TokenClassName tokenClassName) {
		super();
		this.tokenClassName = tokenClassName;
	}

	public IdentifierAutomata(char prefix, TokenClassName tokenClassName) {
		super();
        this.tokenClassName = tokenClassName;
		this.prefix = prefix;
	}

	@Override
	public void reset() {
		this.state = IdentifierAutomataState.Init;
		this.chars = new ArrayList<Character>();
	}
	
	@Override
	public boolean isAvailable() {
		return this.state != IdentifierAutomataState.Fail;
	}
	
	@Override
	public boolean isComplete() {
		return this.state == IdentifierAutomataState.First || this.state == IdentifierAutomataState.Next;
	}
	
	@Override
	public void feed(char c) {
		this.chars.add(c);
		this.state = this.update(c);
	}

	private IdentifierAutomataState update(char c) {
		String s = "" + c;
		
		if (this.state == IdentifierAutomataState.Init) {
			if (this.prefix == null && s.matches("[a-z]")) {
				return IdentifierAutomataState.First;
			}
            else if (this.prefix != null && c == this.prefix) {
                return IdentifierAutomataState.First;
            }
			return IdentifierAutomataState.Fail;
		}
		
		if (this.state == IdentifierAutomataState.First || this.state == IdentifierAutomataState.Next) {
			if (s.matches("[a-zA-Z0-9_]")) {
				return IdentifierAutomataState.Next;
			}
			return IdentifierAutomataState.Fail;
		}
		
		return IdentifierAutomataState.Fail;
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
		return this.tokenClassName;
	}
}
