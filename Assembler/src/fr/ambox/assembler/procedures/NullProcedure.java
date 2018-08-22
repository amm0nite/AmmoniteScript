package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.NullData;

public class NullProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		context.getStack().push(new NullData());
		return true;
	}
}
