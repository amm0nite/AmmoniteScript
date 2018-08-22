package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.IntegerData;
import fr.ambox.assembler.exceptions.ExpectedIntegerDataException;

public class CopyProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws ExpectedIntegerDataException {
		Data d = context.getStack().pop();
		
		if (!(d instanceof IntegerData)) {
			throw new ExpectedIntegerDataException();
		}
		
		IntegerData countData = (IntegerData) d;
		int count = countData.intValue();
		Data[] datas = new Data[count];
		for (int i=0; i<count; i++) {
			datas[i] = context.getStack().pop();
		}
		
		for (int i=count-1; i>=0; i--) {
			context.getStack().push(datas[i]);
		}
		for (int i=count-1; i>=0; i--) {
			context.getStack().push(datas[i]);
		}

		return true;
	}
}
