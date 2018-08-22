package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtIfMember extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(new Terminal(TokenClassName.KeywordIf))
                .add(grammar.nt("expression"))
                .add(grammar.nt("block"));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate();
    }
}
