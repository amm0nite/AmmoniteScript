package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.Stack;

public class DupProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		Stack stack = context.getStack();
		Data d = stack.pop();
		stack.push(d);
		stack.push(d);
		return true;
	}
}
