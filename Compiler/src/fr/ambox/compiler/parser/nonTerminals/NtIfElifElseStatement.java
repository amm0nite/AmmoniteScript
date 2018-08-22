package fr.ambox.compiler.parser.nonTerminals;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.compiler.generator.Renderable;
import fr.ambox.compiler.generator.Template;
import fr.ambox.compiler.parser.Grammar;

import java.util.ArrayList;

public class NtIfElifElseStatement extends fr.ambox.compiler.parser.NonTerminal {
    @Override
    public void defineProductions(Grammar grammar) {
        this.newProduction()
                .add(grammar.nt("ifMember"))
                .add(grammar.nt("elifMember"))
                .add(grammar.nt("elifMemberStar"))
                .add(grammar.nt("elseMember"));

        this.newProduction()
                .add(grammar.nt("ifMember"))
                .add(grammar.nt("elifMember"))
                .add(grammar.nt("elifMemberStar"));
    }

    @Override
    public void defineTemplate(Grammar grammar) {
        this.setRenderer(new Renderable() {
            @Override
            public AtomList render(AtomListBag bag) {
                AtomList elseCode = new AtomList();
                if (bag.has("NtElseMember")) {
                    elseCode = bag.get("NtElseMember");
                }

                ArrayList<AtomList> elifs = new ArrayList<AtomList>();
                for (int i=0; i<bag.size(); i++) {
                    if (bag.getKey(i).contains("NtElifMember")) {
                        elifs.add(bag.get(i));
                    }
                }
                for (int j=(elifs.size() - 1); j>=0; j--) {
                    elseCode.add(new CallAtom("ifelse"));
                    elseCode.prepend(elifs.get(j));
                    elseCode.addFirst(new CallAtom("blstart"));
                    elseCode.add(new CallAtom("blstop"));
                }

                Template template = new Template();
                template.slot("NtIfMember").code(elseCode).call("ifelse");
                return template.render(bag);
            }
        });
    }
}
