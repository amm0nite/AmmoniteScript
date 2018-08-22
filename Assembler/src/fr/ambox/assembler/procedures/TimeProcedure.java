package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.IntegerData;

public class TimeProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		context.getStack().push(new IntegerData(System.currentTimeMillis()));
		return true;
	}

}
