package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.Stack;

public class ExchangeProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		Stack stack = context.getStack();

		Data d2 = stack.pop();
		Data d1 = stack.pop();
		
		stack.push(d2);
		stack.push(d1);

		return true;
	}
}
