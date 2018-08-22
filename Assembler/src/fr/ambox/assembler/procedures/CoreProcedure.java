package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;

public class CoreProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		context.getStack().push(context.getDictStack());
		return true;
	}

}
