package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;

public class RestoreProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		context.getStack().clear();
		Data[] save = context.getSavedStacks().pop();
		for (Data d: save) {
			context.getStack().add(d);
		}
		return true;
	}

}
