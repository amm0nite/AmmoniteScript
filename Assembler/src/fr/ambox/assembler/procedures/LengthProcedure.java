package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.IntegerData;
import fr.ambox.assembler.datas.LengthData;
import fr.ambox.assembler.exceptions.ExpectedLengthDataException;

public class LengthProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws ExpectedLengthDataException {
		Data d = context.getStack().pop();
		
		if (!(d instanceof LengthData)) {
			throw new ExpectedLengthDataException();
		}
		
		LengthData data = (LengthData) d;
		context.getStack().push(new IntegerData(data.getLength()));
		return true;
	}
}
