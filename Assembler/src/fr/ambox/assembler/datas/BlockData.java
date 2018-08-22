package fr.ambox.assembler.datas;

import fr.ambox.assembler.*;
import fr.ambox.assembler.atoms.CallAtom;

public class BlockData extends Data implements CodeData {

	private AtomList atoms;

	public BlockData(AtomList atoms) {
		this.atoms = atoms;
	}

    @Override
    public boolean isEqualTo(Data data) {
	    return this.isLike(data);
    }

    @Override
    public boolean isLike(Data data) {
        if (data == null) {
            return false;
        }
        if (data == this) {
            return true;
        }
        if (!(data instanceof BlockData)) {
            return false;
        }
        BlockData other = (BlockData) data;
        if (this.atoms.size() != other.atoms.size()) {
            return false;
        }
        for (int i=0; i<this.atoms.size(); i++) {
            String tokenString = this.atoms.get(i).toTokenString();
            String otherTokenString = other.atoms.get(i).toTokenString();

            if (!tokenString.equals(otherTokenString)) {
                return false;
            }
        }
        return true;
    }

    @Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
        sb.append("<Block>");
        sb.append(this.atoms);
        sb.append("</Block>");
        return sb.toString();
	}

    public AtomList toAtomBlock() {
        AtomList result = new AtomList();
        result.add(new CallAtom("blstart"));
        result.append(this.atoms);
        result.add(new CallAtom("blstop"));
        return result;
    }

    public AtomList getAtoms() {
    	return this.atoms;
	}

    @Override
    public AtomList assemble(Context context) {
        return this.atoms;
    }
}
