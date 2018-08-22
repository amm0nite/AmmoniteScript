package fr.ambox.assembler.atoms;

import fr.ambox.assembler.Atom;
import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.exceptions.*;

public class ReferenceAtom extends Atom {
    private final int id;

    public ReferenceAtom(String value) {
        this.id = Integer.parseInt(value.substring(1));
    }

    @Override
    public boolean execute(Context context) throws FatalErrorException {
        Data data = context.getStore().get(this.id);
        if (data == null) {
            throw new UndefinedException("" + this.id, "store");
        }
        context.getStack().push(data);
        return true;
    }

    @Override
    public String toTokenString() {
        return "@" + this.id;
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
        if (!(obj instanceof ReferenceAtom)) {
            return false;
        }
        ReferenceAtom other = (ReferenceAtom) obj;
        return (this.id == other.id);
    }
}
