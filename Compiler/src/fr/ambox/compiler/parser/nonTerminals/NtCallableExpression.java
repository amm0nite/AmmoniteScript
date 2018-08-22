package fr.ambox.compiler.parser.nonTerminals;


import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtCallableExpression extends NonTerminal {

    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(grammar.nt("accessExpression"));

        this.newProduction()
                .add(new Terminal(TokenClassName.ParenthesisBegin))
                .add(grammar.nt("functionCreateExpression"))
                .add(new Terminal(TokenClassName.ParenthesisEnd));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate();
    }
}
