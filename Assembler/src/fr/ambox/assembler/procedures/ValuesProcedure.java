package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.ArrayData;
import fr.ambox.assembler.datas.TableData;
import fr.ambox.assembler.exceptions.ExpectedTableDataException;

public class ValuesProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws ExpectedTableDataException {
		Data d = context.getStack().pop();
		
		if (!(d instanceof TableData)) {
			throw new ExpectedTableDataException();
		}
		
		TableData dict = (TableData) d;
		ArrayData array = new ArrayData();
		for (Data value: dict.getMap().values()) {
			array.append(value);
		}
		context.getStack().push(array);

		return true;
	}
}
