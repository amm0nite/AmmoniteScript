package fr.ambox.compiler.generator.templateElements;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;
import fr.ambox.compiler.generator.Renderable;

public class All implements Renderable {

	@Override
	public AtomList render(AtomListBag bag) {
		AtomList result = new AtomList();
		for (int i = 0; i < bag.size(); i++) {
            result.append(bag.get(i));
        }
        return result;
	}
}
