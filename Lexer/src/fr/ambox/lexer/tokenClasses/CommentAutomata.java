package fr.ambox.lexer.tokenClasses;

import java.util.ArrayList;

import fr.ambox.lexer.TokenClass;
import fr.ambox.lexer.TokenClassName;

public class CommentAutomata implements TokenClass {

	private ArrayList<Character> chars;
	private CommentAutomataState state;
	
	public CommentAutomata() {
		this.reset();
	}
	
	public void reset() {
		this.state = CommentAutomataState.Init;
		this.chars = new ArrayList<Character>();
	}
	
	@Override
	public boolean isAvailable() {
		return this.state != CommentAutomataState.Fail;
	}

	@Override
	public boolean isComplete() {
		return this.state == CommentAutomataState.Return;
	}

	@Override
	public void feed(char c) {
		this.chars.add(c);
		this.state = this.update(c);
	}

	private CommentAutomataState update(char c) {
		if (this.state == CommentAutomataState.Init) {
			if (c == '/') {
				return CommentAutomataState.FirstSlash;
			}
			return CommentAutomataState.Fail;
		}
		
		if (this.state == CommentAutomataState.FirstSlash) {
			if (c == '/') {
				return CommentAutomataState.SecondSlash;
			}
			return CommentAutomataState.Fail;
		}
		
		if (this.state == CommentAutomataState.SecondSlash) {
			if (c == '\n') {
				return CommentAutomataState.Return;
			}
			return CommentAutomataState.SecondSlash;
		}
		
		return CommentAutomataState.Fail;
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
		return TokenClassName.Comment;
	}
}
