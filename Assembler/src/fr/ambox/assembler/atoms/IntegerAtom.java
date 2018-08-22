package fr.ambox.assembler.atoms;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Atom;
import fr.ambox.assembler.datas.IntegerData;

public class IntegerAtom extends Atom {
	private long value;

	public IntegerAtom(String content) {
		this.value = Long.parseLong(content);
	}

	@Override
	public boolean execute(Context context) {
		context.getStack().push(new IntegerData(this.value));
		return true;
	}

	@Override
	public String toTokenString() {
		return String.valueOf(this.value);
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
        if (!(obj instanceof IntegerAtom)) {
            return false;
        }
        IntegerAtom other = (IntegerAtom) obj;
        return (this.value == other.value);
    }
}
