package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtOperationExpression extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(new Terminal(TokenClassName.ParenthesisBegin))
                .add(grammar.nt("expression"))
                .add(new Terminal(TokenClassName.ArithmeticOperator))
                .add(grammar.nt("expression"))
                .add(new Terminal(TokenClassName.ParenthesisEnd));

        this.newProduction()
                .add(new Terminal(TokenClassName.ParenthesisBegin))
                .add(grammar.nt("expression"))
                .add(new Terminal(TokenClassName.ComparisonOperator))
                .add(grammar.nt("expression"))
                .add(new Terminal(TokenClassName.ParenthesisEnd));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate().child(1).child(3).child(2).call("load").call("do");
    }
}
