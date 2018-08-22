package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.atoms.IntegerAtom;
import fr.ambox.assembler.atoms.StringAtom;
import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtFunctionCreateExpression extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(new Terminal(TokenClassName.KeywordFunction))
                .add(new Terminal(TokenClassName.ParenthesisBegin))
                .add(new Terminal(TokenClassName.ParenthesisEnd))
                .add(grammar.nt("block"));

        this.newProduction()
                .add(new Terminal(TokenClassName.KeywordFunction))
                .add(new Terminal(TokenClassName.ParenthesisBegin))
                .add(grammar.nt("functionParameterStar"))
                .add(grammar.nt("functionParameter"))
                .add(new Terminal(TokenClassName.ParenthesisEnd))
                .add(grammar.nt("block"));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        AtomList before = new AtomList();
        before.add(new CallAtom("table"));
        before.add(new CallAtom("array"));

        AtomList after = new AtomList();
        after.add(new IntegerAtom("3"));
        after.add(new CallAtom("reverse"));
        after.add(new CallAtom("exch"));
        after.add(new StringAtom("'args'"));
        after.add(new CallAtom("exch"));
        after.add(new CallAtom("set"));
        after.add(new CallAtom("exch"));
        after.add(new StringAtom("'code'"));
        after.add(new CallAtom("exch"));
        after.add(new CallAtom("set"));
        after.add(new CallAtom("fcreate"));

        this.makeTemplate().code(before).all().code(after);
    }
}
