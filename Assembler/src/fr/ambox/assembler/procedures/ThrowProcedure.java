package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.exceptions.WrapDataException;

public class ThrowProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws WrapDataException {
		throw new WrapDataException(context.getStack().pop());
	}

}
