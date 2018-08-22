package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.atoms.NameAtom;
import fr.ambox.compiler.generator.Renderable;
import fr.ambox.compiler.generator.Template;
import fr.ambox.compiler.parser.Grammar;
import fr.ambox.compiler.parser.NonTerminal;
import fr.ambox.compiler.parser.Terminal;
import fr.ambox.lexer.TokenClassName;

public class NtTryCatchFinallyStatement extends NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(new Terminal(TokenClassName.KeywordTry))
                .add(grammar.nt("block"), "tryBlock")
                .add(new Terminal(TokenClassName.KeywordCatch))
                .add(new Terminal(TokenClassName.ParenthesisBegin))
                .add(new Terminal(TokenClassName.Identifier))
                .add(new Terminal(TokenClassName.ParenthesisEnd))
                .add(grammar.nt("block"), "catchBlock")
                .add(new Terminal(TokenClassName.KeywordFinally))
                .add(grammar.nt("block"), "finallyBlock");

        this.newProduction()
                .add(new Terminal(TokenClassName.KeywordTry))
                .add(grammar.nt("block"), "tryBlock")
                .add(new Terminal(TokenClassName.KeywordCatch))
                .add(new Terminal(TokenClassName.ParenthesisBegin))
                .add(new Terminal(TokenClassName.Identifier))
                .add(new Terminal(TokenClassName.ParenthesisEnd))
                .add(grammar.nt("block"), "catchBlock");

        this.newProduction()
                .add(new Terminal(TokenClassName.KeywordTry))
                .add(grammar.nt("block"), "tryBlock")
                .add(new Terminal(TokenClassName.KeywordFinally))
                .add(grammar.nt("block"), "finallyBlock");
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        AtomList emptyCatchCode = new AtomList();
        emptyCatchCode.add(new NameAtom(":e"));
        emptyCatchCode.add(new CallAtom("blstart"));
        emptyCatchCode.add(new CallAtom("blstop"));

        AtomList emptyFinallyCode = new AtomList();
        emptyFinallyCode.add(new CallAtom("blstart"));
        emptyFinallyCode.add(new CallAtom("blstop"));

        Template tcf = (new Template()).all().call("trycf");
        Template tc = (new Template()).all().code(emptyFinallyCode).call("trycf");
        Template tf = (new Template()).child(1).code(emptyCatchCode).allAfter(1).call("trycf");

        this.setRenderer(new Renderable() {
            @Override
            public AtomList render(AtomListBag bag) {
                if (bag.has("tryBlock") && bag.has("catchBlock") && bag.has("finallyBlock")) {
                    return tcf.render(bag);
                }
                if (bag.has("tryBlock") && bag.has("catchBlock") && !bag.has("finallyBlock")) {
                    return tc.render(bag);
                }
                if (bag.has("tryBlock") && !bag.has("catchBlock") && bag.has("finallyBlock")) {
                    return tf.render(bag);
                }

                return new AtomList();
            }
        });
    }
}
