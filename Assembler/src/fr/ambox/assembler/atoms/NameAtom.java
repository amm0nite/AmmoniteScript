package fr.ambox.assembler.atoms;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Atom;
import fr.ambox.assembler.datas.NameData;

public class NameAtom extends Atom {

	private String value;

	public NameAtom(String content) {
		this.value = content.substring(1);
	}

	@Override
	public boolean execute(Context context) {
		context.getStack().push(new NameData(this.value));
		return true;
	}

	@Override
	public String toTokenString() {
		return ":" + this.value;
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
        if (!(obj instanceof NameAtom)) {
            return false;
        }
        NameAtom other = (NameAtom) obj;
        return this.value.equals(other.value);
    }
}
