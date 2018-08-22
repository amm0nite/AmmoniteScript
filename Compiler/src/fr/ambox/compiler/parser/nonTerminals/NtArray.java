package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtArray extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(new Terminal(TokenClassName.ArrayBegin))
                .add(new Terminal(TokenClassName.ArrayEnd));

        this.newProduction()
                .add(new Terminal(TokenClassName.ArrayBegin))
                .add(grammar.nt("arrayMemberStar"))
                .add(grammar.nt("arrayMember"))
                .add(new Terminal(TokenClassName.ArrayEnd));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.makeTemplate().call("array").all();
    }
}
