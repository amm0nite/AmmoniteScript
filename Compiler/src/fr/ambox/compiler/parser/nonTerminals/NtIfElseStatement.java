package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtIfElseStatement extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(grammar.nt("ifMember"))
                .add(grammar.nt("elseMember"));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate().all().call("ifelse");
    }
}
