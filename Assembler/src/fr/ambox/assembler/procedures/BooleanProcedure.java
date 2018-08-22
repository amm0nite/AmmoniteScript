package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.BooleanData;

public abstract class BooleanProcedure implements Procedure {

	private boolean value;
	
	public BooleanProcedure(boolean bool) {
		this.value = bool;
	}

	@Override
	public boolean execute(Context context) {
		context.getStack().push(new BooleanData(this.value));
		return true;
	}
}
