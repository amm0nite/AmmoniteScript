package fr.ambox.compiler.generator.templateElements;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;
import fr.ambox.compiler.generator.Renderable;

public class AllReversed implements Renderable {

	@Override
	public AtomList render(AtomListBag bag) {
		AtomList reversed = new AtomList();
		for (int i = (bag.size() - 1); i >= 0; i--) {
            reversed.append(bag.get(i));
        }
        return reversed;
	}
}
