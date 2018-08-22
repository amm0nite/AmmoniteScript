package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.ArrayData;
import fr.ambox.assembler.datas.IntegerData;
import fr.ambox.assembler.exceptions.ExpectedIntegerDataException;

public class RangeProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws ExpectedIntegerDataException {
		Data d = context.getStack().pop();
		
		if (!(d instanceof IntegerData)) {
			throw new ExpectedIntegerDataException();
		}
		
		IntegerData range = (IntegerData) d;
		ArrayData array = new ArrayData();
		for (int i=0; i<range.intValue(); i++) {
			array.append(new IntegerData(i));
		}
		context.getStack().push(array);

		return true;
	}
}
