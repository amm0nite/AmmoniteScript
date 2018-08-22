package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtForKeyValueStatement extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(new Terminal(TokenClassName.KeywordFor))
                .add(new Terminal(TokenClassName.Identifier))
                .add(new Terminal(TokenClassName.ValueSeparator))
                .add(new Terminal(TokenClassName.Identifier))
                .add(new Terminal(TokenClassName.KeywordIn))
                .add(grammar.nt("expression"))
                .add(grammar.nt("block"));

        this.newProduction()
                .add(new Terminal(TokenClassName.KeywordFor))
                .add(new Terminal(TokenClassName.ParenthesisBegin))
                .add(new Terminal(TokenClassName.Identifier))
                .add(new Terminal(TokenClassName.ValueSeparator))
                .add(new Terminal(TokenClassName.Identifier))
                .add(new Terminal(TokenClassName.KeywordIn))
                .add(grammar.nt("expression"))
                .add(new Terminal(TokenClassName.ParenthesisEnd))
                .add(grammar.nt("block"));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate().all().call("forkv");
    }
}
