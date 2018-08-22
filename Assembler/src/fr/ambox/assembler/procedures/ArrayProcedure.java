package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.ArrayData;

public class ArrayProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		context.getStack().push(new ArrayData());
		return true;
	}

}
