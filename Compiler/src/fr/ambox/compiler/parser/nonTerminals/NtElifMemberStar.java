package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminalStar;

public class NtElifMemberStar extends NonTerminalStar {

    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction().add(grammar.nt("elifMember"));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate();
    }
}
