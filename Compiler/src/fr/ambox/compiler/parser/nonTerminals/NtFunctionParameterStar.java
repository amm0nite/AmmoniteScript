package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminalStar;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtFunctionParameterStar extends NonTerminalStar {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(new Terminal(TokenClassName.Identifier))
                .add(new Terminal(TokenClassName.ValueSeparator));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate().all().call("append");
    }
}
