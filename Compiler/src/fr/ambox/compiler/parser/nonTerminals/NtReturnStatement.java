package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtReturnStatement extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(grammar.nt("returnExpression"))
                .add(new Terminal(TokenClassName.Terminator));

        this.newProduction()
                .add(grammar.nt("returnEmpty"))
                .add(new Terminal(TokenClassName.Terminator));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate().all().call("return");
    }
}
