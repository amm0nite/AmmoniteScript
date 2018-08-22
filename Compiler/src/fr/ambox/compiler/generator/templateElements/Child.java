package fr.ambox.compiler.generator.templateElements;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;
import fr.ambox.compiler.generator.Renderable;

import java.util.LinkedHashMap;

public class Child implements Renderable {

	private int index;

	public Child(int i) {
		this.index = i - 1;
	}


	@Override
	public AtomList render(AtomListBag bag) {
		return bag.get(index);
	}
}
