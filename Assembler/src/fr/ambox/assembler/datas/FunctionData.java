package fr.ambox.assembler.datas;

import fr.ambox.assembler.*;
import fr.ambox.assembler.atoms.*;

import java.util.ArrayList;

public class FunctionData extends Data implements CodeData {

    private ArrayData args;
    private BlockData code;

    private TableData locals;
    private String name;
    private Data parent;

    public FunctionData(ArrayData args, BlockData code, TableData locals) {
        this.args = args;
        this.code = code;
        this.locals = locals;
		this.parent = new TableData();
        this.name = "anonymous";
	}

    public FunctionData(ArrayData args, BlockData code) {
        this(args, code, new TableData());
    }

    public FunctionData() {
        this(new ArrayData(), new BlockData(new AtomList()));
    }

    private ArrayList<String> getArguments() {
        ArrayList<String> arguments = new ArrayList<String>();
        for (Data argData: this.args) {
            if (argData instanceof NameData) {
                NameData argName = (NameData) argData;
                arguments.add(argName.getValue());
            }
        }
        return arguments;
    }

    public String toString() { return "<Function>" + this.code + "</Function>"; }

	@Override
	public AtomList assemble(Context context) {
		AtomList atoms = new AtomList();

        // begin
		atoms.add(new CallAtom("begin"));

        // load saved locals
        int localsId = context.getStore().add(this.locals);
        atoms.add(new ReferenceAtom("@" + String.valueOf(localsId)));
        atoms.add(new CallAtom("import"));

        // function name
        atoms.add(new NameAtom(":_function"));
        atoms.add(new StringAtom("'" + this.name + "'"));
        atoms.add(new CallAtom("def"));

        // push to callstack
        atoms.add(new VariableAtom("$_function"));
        atoms.add(new VariableAtom("$_line"));
        atoms.add(new CallAtom("call"));

        // set THIS
        atoms.add(new NameAtom(":this"));
        int parentId = context.getStore().add(this.parent);
        atoms.add(new ReferenceAtom("@" + parentId));
        atoms.add(new CallAtom("def"));

        // code
        for (String arg: this.getArguments()) {
            atoms.add(new NameAtom(":" + arg));
            atoms.add(new CallAtom("exch"));
            atoms.add(new CallAtom("def"));
        }
        atoms.append(code.toAtomBlock());
        atoms.add(new CallAtom("do"));

        // pop callstack
        atoms.add(new CallAtom("uncall"));

        // end
		atoms.add(new CallAtom("end"));

        // free
        atoms.add(new IntegerAtom(String.valueOf(localsId)));
        atoms.add(new CallAtom("free"));
        atoms.add(new IntegerAtom(String.valueOf(parentId)));
        atoms.add(new CallAtom("free"));

		return atoms;
	}

	@Override
    public boolean isEqualTo(Data data) {
        return (data != null && data == this);
    }

    @Override
    public boolean isLike(Data data) {
        if (data == null) {
            return false;
        }
        if (data == this) {
            return true;
        }
        if (!(data instanceof FunctionData)) {
            return false;
        }
        FunctionData other = (FunctionData) data;
        if (!other.name.equals(this.name)) {
            return false;
        }
        if (!other.args.isLike(this.args)) {
            return false;
        }
        if (!other.code.isLike(this.code)) {
            return false;
        }
        if (!other.locals.isLike(this.locals)) {
            return false;
        }
        if (!other.parent.isLike(this.parent)) {
            return false;
        }
        return true;
    }

    public ArrayData getArgs() {
        return this.args;
    }

    public void setArgs(ArrayData args) {
        this.args = args;
    }

    public BlockData getCode() {
        return this.code;
    }

    public void setCode(BlockData code) {
        this.code = code;
    }

    public TableData getLocals() {
        return this.locals;
    }

    public void setLocals(TableData locals) {
        this.locals = locals;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Data getParent() {
        return this.parent;
    }

    public void setParent(Data parent) {
        this.parent = parent;
    }

    public void mimic(FunctionData function) {
        this.args = function.args;
        this.code = function.code;
        this.locals = function.locals;
        this.name = function.name;
        this.parent = function.parent;
    }
}
