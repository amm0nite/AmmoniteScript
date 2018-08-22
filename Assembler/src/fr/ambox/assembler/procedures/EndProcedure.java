package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;

public class EndProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		context.getDictStack().pop();
		return true;
	}

}
