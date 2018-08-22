package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtBlock extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(new Terminal(TokenClassName.ObjectBegin))
                .add(grammar.nt("statementStar"))
                .add(new Terminal(TokenClassName.ObjectEnd));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate().call("blstart").all().call("blstop");
    }
}
