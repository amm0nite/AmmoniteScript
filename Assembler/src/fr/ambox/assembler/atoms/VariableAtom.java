package fr.ambox.assembler.atoms;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Atom;
import fr.ambox.assembler.exceptions.UndefinedException;

public class VariableAtom extends Atom {

	private String name;

	public VariableAtom(String content) {
		this.name = content.substring(1);
	}

	@Override
	public boolean execute(Context context) throws UndefinedException {
		Data data = context.getDictStack().get(this.name);
		
		if (data == null) {
			throw new UndefinedException(this.name, "dictStack");
		}
		
		context.getStack().push(data);
		return true;
	}

	@Override
	public String toTokenString() {
		return "$" + this.name;
	}

	@Override
	public boolean isBlockBoundary() {
		return false;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof VariableAtom)) {
            return false;
        }
        VariableAtom other = (VariableAtom) obj;
        return this.name.equals(other.name);
    }
}
