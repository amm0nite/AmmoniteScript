package fr.ambox.assembler;

public class Step {

    private AtomList atoms;
    private int offset;

    public Step(AtomList atoms, int offset) {
        this.atoms = atoms;
        this.offset = offset;
    }

    public Step(AtomList atoms) {
        this(atoms, 0);
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String toString() {
        String res = "";
        res += this.atoms.toString();
        res += "(" + this.offset + ")";
        return res;
    }

    public AtomList getAtoms() {
        return atoms;
    }

    public int getOffset() {
        return offset;
    }
}
