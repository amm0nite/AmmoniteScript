package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.BooleanData;
import fr.ambox.assembler.datas.NullData;

public class IsNullProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		Data d = context.getStack().pop();
		context.getStack().push(new BooleanData(d instanceof NullData));
		return true;
	}
}
