package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminalStar;

public class NtStatementStar extends NonTerminalStar {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction().add(grammar.nt("assignmentStatement"));
        this.newProduction().add(grammar.nt("procedureStatement"));
        this.newProduction().add(grammar.nt("returnStatement"));
        this.newProduction().add(grammar.nt("ifElifElseStatement"));
        this.newProduction().add(grammar.nt("ifElseStatement"));
        this.newProduction().add(grammar.nt("ifStatement"));
        this.newProduction().add(grammar.nt("forValueStatement"));
        this.newProduction().add(grammar.nt("forKeyValueStatement"));
        this.newProduction().add(grammar.nt("whileStatement"));
        this.newProduction().add(grammar.nt("tryCatchFinallyStatement"));
        this.newProduction().add(grammar.nt("throwStatement"));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate();
    }
}
