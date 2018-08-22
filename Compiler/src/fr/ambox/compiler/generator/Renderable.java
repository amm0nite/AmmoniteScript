package fr.ambox.compiler.generator;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;

public interface Renderable {
	AtomList render(AtomListBag bag);
}
