package fr.ambox.compiler.parser;

import fr.ambox.compiler.exceptions.SyntaxErrorException;
import fr.ambox.lexer.Token;
import fr.ambox.lexer.TokenClassName;
import fr.ambox.lexer.TokenList;

import java.util.ArrayList;

public class ParserContext {

	private TokenList tokens;
	private int index;
	
	private int errorIndex;
	private ArrayList<TokenClassName> errorAlternatives;
	
	public ParserContext(TokenList tokens) {
		this.tokens = new TokenList();
		
		ArrayList<TokenClassName> ignoreList = new ArrayList<TokenClassName>();
		ignoreList.add(TokenClassName.Comment);
		ignoreList.add(TokenClassName.CommentBlock);
		ignoreList.add(TokenClassName.WhiteSpace);
		for (Token t: tokens) {
			TokenClassName name = t.getTokenClass().getName();
			if (!ignoreList.contains(name)) {
				this.tokens.add(t);
			}
		}
		
		this.index = 0;
	}

	public Token consume() {
		Token res = this.tokens.get(this.index);
		this.index++;
		return res;
	}
	
	public String toString() {
		return "context@"+this.index;
	}

	public int getIndex() {
		return this.index;
	}

	public void setIndex(int save) {
		this.index = save;
	}
	
	public void errorAdvise(Token token, TokenClassName className) {
		if (this.index > this.errorIndex) {
			this.errorIndex = this.index;
			this.errorAlternatives = new ArrayList<TokenClassName>();
		}
		if (this.index == this.errorIndex) {
			if (!this.errorAlternatives.contains(className)) {
				this.errorAlternatives.add(className);
			}
		}
	}
	
	public SyntaxErrorException makeSyntaxError() {
		Token unexpectedToken = null;
		Token afterToken = null;

		int current = this.errorIndex - 1;
		if (current >= 0) {
			unexpectedToken = this.tokens.get(current);
		}
		int previous = this.errorIndex - 2;
		if (previous >= 0) {
			afterToken = this.tokens.get(previous);
		}

		return new SyntaxErrorException(unexpectedToken, afterToken, this.errorAlternatives);
	}
}
