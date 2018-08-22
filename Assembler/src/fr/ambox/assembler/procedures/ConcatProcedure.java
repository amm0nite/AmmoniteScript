package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.StringData;
import fr.ambox.assembler.datas.TextData;
import fr.ambox.assembler.exceptions.ExpectedTextDataException;

public class ConcatProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws ExpectedTextDataException {
		Data d2 = context.getStack().pop();
		Data d1 = context.getStack().pop();

		if (!(d1 instanceof TextData) || !(d2 instanceof TextData)) {
			throw new ExpectedTextDataException();
		}

		TextData textData1 = (TextData) d1;
		TextData textData2 = (TextData) d2;

		context.getStack().push(new StringData(textData1.stringValue() + textData2.stringValue()));
		return true;
	}
}
