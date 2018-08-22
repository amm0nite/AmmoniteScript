package fr.ambox.compiler.parser.nonTerminals;


import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtFunctionCallExpression extends NonTerminal {

    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(grammar.nt("callableExpression"))
                .add(new Terminal(TokenClassName.ParenthesisBegin))
                .add(new Terminal(TokenClassName.ParenthesisEnd));

        this.newProduction()
                .add(grammar.nt("callableExpression"))
                .add(new Terminal(TokenClassName.ParenthesisBegin))
                .add(grammar.nt("expressionCommaStar"))
                .add(grammar.nt("expression"))
                .add(new Terminal(TokenClassName.ParenthesisEnd));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate().allReversed().call("do");
    }
}
