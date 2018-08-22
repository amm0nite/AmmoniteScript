package fr.ambox.assembler.atoms;

import fr.ambox.assembler.Atom;
import fr.ambox.assembler.Context;
import fr.ambox.assembler.ProcedureState;
import fr.ambox.assembler.datas.IntegerData;
import fr.ambox.assembler.datas.StateData;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class InformationAtom extends Atom {
    private String value;

    public InformationAtom(String value) {
        this.value = value.substring(1);
    }

    @Override
    public boolean execute(Context context) throws FatalErrorException {
        if (this.isLineNumber()) {
            int lineNumber = Integer.parseInt(this.value);
            context.getDictStack().put("_line", new IntegerData(lineNumber));
        }
        else {
            ProcedureState state = ProcedureState.valueOf(this.value);
            context.getStack().push(new StateData(state));
        }
        return true;
    }

    @Override
    public String toTokenString() {
        return "#" + this.value;
    }

    @Override
    public boolean isBlockBoundary() {
        return false;
    }

    public boolean isLineNumber() {
        return this.value.matches("^\\d+$");
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof InformationAtom)) {
            return false;
        }
        InformationAtom other = (InformationAtom) obj;
        return this.value.equals(other.value);
    }
}
