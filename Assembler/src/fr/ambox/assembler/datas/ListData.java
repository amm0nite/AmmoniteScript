package fr.ambox.assembler.datas;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.atoms.VariableAtom;
import fr.ambox.assembler.exceptions.ArrayKeyTypeException;
import fr.ambox.assembler.exceptions.DictOperationException;

public abstract class ListData extends Data implements DictData {

    public abstract Data get(int index) throws DictOperationException;
    public abstract void put(int index, Data value) throws DictOperationException;
    public abstract void append(Data value) throws DictOperationException;

    @Override
    public Data get(String key) throws DictOperationException {
        if (key.equals("append")) {
            return this.makeAppendFunction();
        }
        if (key.equals("length")) {
            return this.makeLengthFunction();
        }
        if (!key.matches("\\d+")) {
            throw new ArrayKeyTypeException();
        }
        int index = Integer.parseInt(key);
        return this.get(index);
    }

    @Override
    public void put(String key, Data value) throws DictOperationException {
        if (!key.matches("\\d+")) {
            throw new ArrayKeyTypeException();
        }
        int index = Integer.parseInt(key);
        this.put(index, value);
    }

    @Override
    public boolean has(String key) throws DictOperationException {
        return (this.get(key) != null);
    }

    private FunctionData makeAppendFunction() {
        ArrayData args = new ArrayData();
        args.append(new NameData("elt"));

        AtomList atoms = new AtomList();
        atoms.add(new VariableAtom("$this"));
        atoms.add(new VariableAtom("$elt"));
        atoms.add(new CallAtom("append"));
        BlockData code = new BlockData(atoms);

        FunctionData appendFunction = new FunctionData(args, code);
        appendFunction.setParent(this);
        return appendFunction;
    }

    private FunctionData makeLengthFunction() {
        ArrayData args = new ArrayData();

        AtomList atoms = new AtomList();
        atoms.add(new VariableAtom("$this"));
        atoms.add(new CallAtom("length"));
        BlockData code = new BlockData(atoms);

        FunctionData lengthFunction = new FunctionData(args, code);
        lengthFunction.setParent(this);
        return lengthFunction;
    }
}