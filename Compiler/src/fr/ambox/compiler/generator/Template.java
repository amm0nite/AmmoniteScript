package fr.ambox.compiler.generator;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;
import fr.ambox.compiler.generator.templateElements.*;

public class Template implements Renderable {
	
	private ArrayList<Renderable> elements;
	
	public Template() {
		this.elements = new ArrayList<Renderable>();
	}

	public Template code(AtomList atoms) {
		this.elements.add(new Code(atoms));
		return this;
	}

	public Template all() {
		this.elements.add(new All());
		return this;
	}

	public Template child(int i) {
		this.elements.add(new Child(i));
		return this;
	}
	
	public Template allAfter(int i) {
		this.elements.add(new AllAfter(i));
		return this;
	}
	
	public Template allReversed() {
		this.elements.add(new AllReversed());
		return this;
	}

	public Template slot(String name) {
		this.elements.add(new Slot(name));
		return this;
	}

	public Template call(String name) {
		this.elements.add(new Call(name));
		return this;
	}

    @Override
    public AtomList render(AtomListBag bag) {
        AtomList atoms = new AtomList();
        if (this.elements.isEmpty()) {
            this.all();
        }
        for (Renderable renderable: this.elements) {
            atoms.append(renderable.render(bag));
        }
        return atoms;
    }
}
