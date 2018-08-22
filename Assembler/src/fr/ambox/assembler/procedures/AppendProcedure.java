package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.ArrayData;
import fr.ambox.assembler.exceptions.ExpectedArrayDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class AppendProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws FatalErrorException {
		Data d2 = context.getStack().pop();
		Data d1 = context.getStack().pop();
		
		if (!(d1 instanceof ArrayData)) {
			throw new ExpectedArrayDataException();
		}

		ArrayData array = (ArrayData) d1;
		Data element = d2;
		array.append(element);
		context.getStack().push(array);
		return true;
	}
}
