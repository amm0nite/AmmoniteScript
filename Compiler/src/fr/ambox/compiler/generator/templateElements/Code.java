package fr.ambox.compiler.generator.templateElements;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;
import fr.ambox.compiler.generator.Renderable;

public class Code implements Renderable {

	private AtomList atoms;

	public Code(AtomList atoms) {
		this.atoms = atoms;
	}

	@Override
	public AtomList render(AtomListBag bag) {
		return this.atoms;
	}
}
