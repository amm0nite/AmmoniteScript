package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtObjectMember extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(grammar.nt("nameExpression"))
                .add(new Terminal(TokenClassName.KeyValueSeparator))
                .add(grammar.nt("expression"));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate().all().call("set");
    }
}
