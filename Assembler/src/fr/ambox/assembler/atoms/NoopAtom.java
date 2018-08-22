package fr.ambox.assembler.atoms;

import fr.ambox.assembler.Atom;
import fr.ambox.assembler.Context;

public class NoopAtom extends Atom {
    @Override
    public boolean execute(Context context) {
        return true;
    }

    @Override
    public String toTokenString() {
        return "";
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
        return (obj instanceof NoopAtom);
    }
}
