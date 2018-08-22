package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;

public class SaveProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		Data[] save = context.getStack().toArray();
		context.getSavedStacks().push(save);
		return true;
	}

}
