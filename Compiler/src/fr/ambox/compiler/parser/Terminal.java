package fr.ambox.compiler.parser;

import fr.ambox.lexer.Token;
import fr.ambox.lexer.TokenClassName;

public class Terminal implements Symbol {

	private TokenClassName className;

	public Terminal(TokenClassName tcn) {
		this.className = tcn;
	}
	
	@Override
	public ParseNodeList test(ParserContext context) {
		Token token = context.consume();
		boolean res = (this.className == token.getTokenClass().getName());
		if (!res) {
			context.errorAdvise(token, this.className);
			return null;
		}
		return new ParseNodeList(new ParseNode(new ParsedToken(token)));
	}
}
