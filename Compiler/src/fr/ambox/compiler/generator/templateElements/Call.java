package fr.ambox.compiler.generator.templateElements;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.compiler.generator.Renderable;

public class Call implements Renderable {
    private String call;

    public Call(String call) {
        this.call = call;
    }

    @Override
    public AtomList render(AtomListBag bag) {
        AtomList result = new AtomList();
        result.add(new CallAtom(this.call));
        return result;
    }
}
