package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminalStar;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtAssignmentAccessStar extends NonTerminalStar {

    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(new Terminal(TokenClassName.ArrayBegin))
                .add(grammar.nt("expression"))
                .add(new Terminal(TokenClassName.ArrayEnd));

        this.newProduction()
                .add(new Terminal(TokenClassName.Dot))
                .add(new Terminal(TokenClassName.Identifier));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate().call("get").all();
    }
}
