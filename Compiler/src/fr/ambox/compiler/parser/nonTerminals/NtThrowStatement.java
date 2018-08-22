package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtThrowStatement extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(new Terminal(TokenClassName.KeywordThrow))
                .add(grammar.nt("expression"))
                .add(new Terminal(TokenClassName.Terminator));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate().all().call("throw");
    }
}
