package fr.ambox.lexer.tokenClasses;

import java.util.ArrayList;

import fr.ambox.lexer.TokenClass;
import fr.ambox.lexer.TokenClassName;

public class CommentBlockAutomata implements TokenClass {

	private ArrayList<Character> chars;
	private CommentBlockAutomataState state;
	
	public CommentBlockAutomata() {
		this.reset();
	}
	
	public void reset() {
		this.state = CommentBlockAutomataState.Init;
		this.chars = new ArrayList<Character>();
	}
	
	@Override
	public boolean isAvailable() {
		return this.state != CommentBlockAutomataState.Fail;
	}

	@Override
	public boolean isComplete() {
		return this.state == CommentBlockAutomataState.EndSlash;
	}

	@Override
	public void feed(char c) {
		this.chars.add(c);
		this.state = this.update(c);
	}

	private CommentBlockAutomataState update(char c) {
		if (this.state == CommentBlockAutomataState.Init) {
			if (c == '/') {
				return CommentBlockAutomataState.BeginSlash;
			}
			return CommentBlockAutomataState.Fail;
		}
		
		if (this.state == CommentBlockAutomataState.BeginSlash) {
			if (c == '*') {
				return CommentBlockAutomataState.BeginStar;
			}
			return this.state = CommentBlockAutomataState.Fail;
		}
		
		if (this.state == CommentBlockAutomataState.BeginStar) {
			if (c == '*') {
				return CommentBlockAutomataState.EndStar;
			}
			return CommentBlockAutomataState.BeginStar;
		}
		
		if (this.state == CommentBlockAutomataState.EndStar) {
			if (c == '/') {
				return CommentBlockAutomataState.EndSlash;
			}
			return CommentBlockAutomataState.BeginStar;
		}
		
		return CommentBlockAutomataState.Fail;
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
		return TokenClassName.CommentBlock;
	}
}
