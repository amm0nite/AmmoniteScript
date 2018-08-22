package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtValueExpression extends NonTerminal {

    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction().add(grammar.nt("array"));
        this.newProduction().add(grammar.nt("object"));
        this.newProduction().add(new Terminal(TokenClassName.Number));
        this.newProduction().add(new Terminal(TokenClassName.String));
        this.newProduction().add(new Terminal(TokenClassName.KeywordNull));
        this.newProduction().add(new Terminal(TokenClassName.KeywordTrue));
        this.newProduction().add(new Terminal(TokenClassName.KeywordFalse));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate();
    }
}
