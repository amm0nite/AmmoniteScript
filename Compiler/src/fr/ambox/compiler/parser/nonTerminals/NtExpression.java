package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtExpression extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction().add(grammar.nt("functionCallExpression"));
        this.newProduction().add(grammar.nt("functionCreateExpression"));
        this.newProduction().add(grammar.nt("operationExpression"));
        this.newProduction().add(grammar.nt("accessExpression"));
        this.newProduction().add(grammar.nt("valueExpression"));

        this.newProduction() // extra parenthesis-ed expression
            .add(new Terminal(TokenClassName.ParenthesisBegin))
            .add(this)
            .add(new Terminal(TokenClassName.ParenthesisEnd));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate();
    }
}
