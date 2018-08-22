package fr.ambox.assembler.datas;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.atoms.VariableAtom;
import fr.ambox.assembler.exceptions.DictOperationException;

public final class StringData extends Data implements TextData, LengthData, DictData {
    private final String value;

    public StringData(String value) {
		this.value = value;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StringData)) {
            return false;
        }
        StringData other = (StringData) obj;
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean isEqualTo(Data data) {
        return this.equals(data);
    }

    @Override
    public boolean isLike(Data data) {
        return this.equals(data);
    }

    @Override
    public String toString() {
        return "'"+this.stringValue().replace("'", "\\'")+"'";
    }

    @Override
    public String stringValue() {
        return this.value;
    }

    @Override
    public int getLength() {
        return this.value.length();
    }


    @Override
    public Data get(String key) throws DictOperationException {
        if (key.equals("length")) {
            return this.makeLengthFunction();
        }
        if (key.equals("charAt")) {
            return this.makeCharAtFunction();
        }
        return null;
    }

    private Data makeCharAtFunction() {
        ArrayData args = new ArrayData();
        args.append(new NameData("index"));

        AtomList atoms = new AtomList();
        atoms.add(new VariableAtom("$this"));
        atoms.add(new VariableAtom("$index"));
        atoms.add(new CallAtom("charAt"));
        BlockData code = new BlockData(atoms);

        FunctionData lengthFunction = new FunctionData(args, code);
        lengthFunction.setParent(this);
        return lengthFunction;
    }

    private Data makeLengthFunction() {
        ArrayData args = new ArrayData();

        AtomList atoms = new AtomList();
        atoms.add(new VariableAtom("$this"));
        atoms.add(new CallAtom("length"));
        BlockData code = new BlockData(atoms);

        FunctionData lengthFunction = new FunctionData(args, code);
        lengthFunction.setParent(this);
        return lengthFunction;
    }

    @Override
    public void put(String key, Data value) throws DictOperationException {

    }

    @Override
    public boolean has(String key) throws DictOperationException {
        return (this.get(key) != null);
    }

    @Override
    public String[] keys() {
        return new String[0];
    }
}
