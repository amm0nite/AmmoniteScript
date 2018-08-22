package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.Step;
import fr.ambox.assembler.datas.CodeData;
import fr.ambox.assembler.datas.ProcedureData;
import fr.ambox.assembler.exceptions.*;

public class DoProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws FatalErrorException {
        Data d = context.getStack().pop();

		if (d instanceof ProcedureData) {
			ProcedureData procedure = (ProcedureData) d;
			return procedure.execute(context);
		}

        if (d instanceof CodeData) {
            CodeData code = (CodeData) d;
            context.getBootstrap().push(new Step(code.assemble(context), 0));
            return true;
        }

        throw new ExpectedCodeOrProcedureData();
	}
}
