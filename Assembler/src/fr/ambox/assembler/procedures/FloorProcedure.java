package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.DecimalData;
import fr.ambox.assembler.datas.DoubleData;
import fr.ambox.assembler.exceptions.ExpectedDoubleDataException;

public class FloorProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws ExpectedDoubleDataException {
		Data d = context.getStack().pop();
		
		if (!(d instanceof DoubleData)) {
			throw new ExpectedDoubleDataException();
		}
		
		DoubleData value = (DoubleData) d;
		context.getStack().push(new DecimalData(Math.floor(value.doubleValue())));
		return true;
	}

}
