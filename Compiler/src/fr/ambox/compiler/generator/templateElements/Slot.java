package fr.ambox.compiler.generator.templateElements;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;
import fr.ambox.compiler.generator.Renderable;

public class Slot implements Renderable {
    private String name;

    public Slot(String name) {
        this.name = name;
    }

    @Override
    public AtomList render(AtomListBag bag) {
        return bag.get(this.name);
    }
}
