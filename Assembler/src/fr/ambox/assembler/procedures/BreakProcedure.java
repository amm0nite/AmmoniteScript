package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.SkipState;

public class BreakProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		context.setSkipState(SkipState.Break);
		return true;
	}
}
