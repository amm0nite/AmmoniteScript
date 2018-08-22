package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.Stack;
import fr.ambox.assembler.datas.BooleanData;

public class EqualsProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
        Stack stack = context.getStack();
		Data d2 = stack.pop();
		Data d1 = stack.pop();
		stack.push(new BooleanData(d1.isEqualTo(d2)));
		return true;
	}
}
