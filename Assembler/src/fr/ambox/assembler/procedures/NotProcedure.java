package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.BooleanData;
import fr.ambox.assembler.exceptions.ExpectedBooleanDataException;

public class NotProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws ExpectedBooleanDataException {
		Data d = context.getStack().pop();
		
		if (!(d instanceof BooleanData)) {
			throw new ExpectedBooleanDataException(d);
		}

		BooleanData bool = (BooleanData) d;
		context.getStack().push(new BooleanData(!bool.getValue()));
		return true;
	}

}
