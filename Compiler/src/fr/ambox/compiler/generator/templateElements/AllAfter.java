package fr.ambox.compiler.generator.templateElements;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;
import fr.ambox.compiler.generator.Renderable;

public class AllAfter implements Renderable {

	private int index;

	public AllAfter(int i) {
		this.index = i - 1;
	}

	@Override
	public AtomList render(AtomListBag bag) {
        int start = this.index + 1;
        AtomList result = new AtomList();
		for (int i = start; i < bag.size(); i++) {
            result.append(bag.get(i));
        }
        return result;
	}
}
