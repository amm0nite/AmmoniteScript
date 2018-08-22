package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.atoms.IntegerAtom;
import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtAssignmentCombinedOperator extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(grammar.nt("assignmentAccessBlob"))
                .add(new Terminal(TokenClassName.ArithmeticOperator))
                .add(new Terminal(TokenClassName.Assignator))
                .add(grammar.nt("expression"));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        AtomList copy = new AtomList();
        copy.add(new IntegerAtom("2"));
        copy.add(new CallAtom("copy"));
        copy.add(new CallAtom("get"));

        this.makeTemplate().child(1).code(copy).child(3).child(2).call("load").call("do");
    }
}
