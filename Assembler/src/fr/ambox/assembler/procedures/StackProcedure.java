package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.ArrayData;

public class StackProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		Data[] datas = context.getStack().toArray();
		ArrayData array = new ArrayData();
		for (Data d: datas) {
			array.append(d);
		}
		context.getStack().push(array);
		return true;
	}
}
