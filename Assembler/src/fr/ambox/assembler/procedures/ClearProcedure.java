package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;

public class ClearProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		context.getStack().clear();
		return true;
	}

}
