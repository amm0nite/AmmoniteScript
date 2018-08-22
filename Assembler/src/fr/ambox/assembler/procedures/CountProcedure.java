package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.IntegerData;

public class CountProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		context.getStack().push(new IntegerData(context.getStack().size()));
		return true;
	}
}
