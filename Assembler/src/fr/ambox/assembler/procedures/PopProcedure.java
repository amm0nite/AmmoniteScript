package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;

public class PopProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		context.getStack().pop();
		return true;
	}
}
