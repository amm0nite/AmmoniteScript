package fr.ambox.assembler.atoms;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Atom;
import fr.ambox.assembler.datas.DecimalData;

public class DecimalAtom extends Atom {
	private double value;
	
	public DecimalAtom(String content) {
		this.value = Double.parseDouble(content);
	}

	@Override
	public boolean execute(Context context) {
		context.getStack().push(new DecimalData(this.value));
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
        if (!(obj instanceof DecimalAtom)) {
            return false;
        }
        DecimalAtom other = (DecimalAtom) obj;
        return (this.value == other.value);
    }
}
