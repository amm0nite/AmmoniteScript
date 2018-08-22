package fr.ambox.compiler.parser;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.AtomListBag;

public interface ParsedElement {
	/**
	 * Return the assembly code for the terminal or non-terminal
	 */
	AtomList generate(AtomListBag bag);

	String getName();
}
