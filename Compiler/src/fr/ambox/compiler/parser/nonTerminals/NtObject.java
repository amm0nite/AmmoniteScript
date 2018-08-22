package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtObject extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(new Terminal(TokenClassName.ObjectBegin))
                .add(new Terminal(TokenClassName.ObjectEnd));

        this.newProduction()
                .add(new Terminal(TokenClassName.ObjectBegin))
                .add(grammar.nt("objectMemberStar"))
                .add(grammar.nt("objectMember"))
                .add(new Terminal(TokenClassName.ObjectEnd));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        AtomList code = new AtomList();
        code.add(new CallAtom("table"));
        this.makeTemplate().code(code).all();
    }
}
