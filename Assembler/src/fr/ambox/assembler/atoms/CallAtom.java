package fr.ambox.assembler.atoms;

import fr.ambox.assembler.*;
import fr.ambox.assembler.datas.CodeData;
import fr.ambox.assembler.datas.ProcedureData;
import fr.ambox.assembler.exceptions.*;

import java.util.HashMap;

public class CallAtom extends Atom {
    private static HashMap<String, String> translationMatrix;

	private String name;

	public CallAtom(String name) {
		this.name = name;
	}

    public Data resolveName(Context context) throws UndefinedException {
        Data data = context.getDictStack().get(this.name);
        if (data == null) {
            throw new UndefinedException(this.name, "dictStack");
        }
        return data;
    }

	@Override
	public boolean execute(Context context) throws FatalErrorException {
		Data data = this.resolveName(context);

        if (data instanceof ProcedureData) {
            ProcedureData procedureData = (ProcedureData) data;
            return procedureData.execute(context);
        }
        else if (data instanceof CodeData) {
            CodeData codeData = (CodeData) data;
            Step step = new Step(codeData.assemble(context));
            context.getBootstrap().push(step);
            return true;
        }
        else {
            throw new ExpectedCodeOrProcedureData();
        }
	}

    @Override
    public String toTokenString() {
        return this.name;
    }

    @Override
    public boolean isBlockBoundary() {
        return (this.name.equals("blstart") || this.name.equals("blstop"));
    }

    public static String translateOperator(String lexeme) {
        if (translationMatrix == null) {
            translationMatrix = new HashMap<String, String>();
            translationMatrix.put("+", "add");
            translationMatrix.put("-", "sub");
            translationMatrix.put("*", "mul");
            translationMatrix.put("/", "div");
            translationMatrix.put("%", "mod");
            translationMatrix.put("==", "eq");
            translationMatrix.put(">", "gt");
            translationMatrix.put("<", "lt");
            translationMatrix.put(">=", "ge");
            translationMatrix.put("<=", "le");
            translationMatrix.put("{", "blstart");
            translationMatrix.put("}", "blstop");
        }

        String translation = translationMatrix.get(lexeme);
        if (translation == null) {
            throw new RuntimeException("Missing translation for operator " + lexeme);
        }
        return translation;
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CallAtom)) {
            return false;
        }
        CallAtom other = (CallAtom) obj;
        return this.name.equals(other.name);
    }
}
