package fr.ambox.compiler.parser;

import fr.ambox.compiler.Assembly;
import fr.ambox.compiler.exceptions.MultipleParseTreeException;
import fr.ambox.compiler.exceptions.SyntaxErrorException;
import fr.ambox.lexer.TokenList;

public class Parser {

    private TokenList tokens;
	private Grammar grammar;

	public Parser(TokenList tokens, Grammar grammar) {
        this.tokens = tokens;
		this.grammar = grammar;
	}

	public Assembly analyse() throws MultipleParseTreeException, SyntaxErrorException {
		ParserContext context = new ParserContext(tokens);
		ParseNodeList list = this.grammar.getStart().test(context);
		
		if (list == null) {
			throw context.makeSyntaxError();
		}
		
		ParseNode ast = list.toParseNode().toAST();
        return new Assembly(ast.generate());
	}
}
