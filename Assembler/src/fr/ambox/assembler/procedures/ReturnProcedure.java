package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.SkipState;

public class ReturnProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		context.setSkipState(SkipState.Return);
		return true;
	}

}
