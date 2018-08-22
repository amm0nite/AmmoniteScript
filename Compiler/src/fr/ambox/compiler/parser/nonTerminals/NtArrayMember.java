package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;

public class NtArrayMember extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(grammar.nt("expression"));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate().all().call("append");
    }
}
