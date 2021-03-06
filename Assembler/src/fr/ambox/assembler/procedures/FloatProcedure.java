package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.DecimalData;
import fr.ambox.assembler.datas.NullData;
import fr.ambox.assembler.datas.TextData;

public class FloatProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		Data d = context.getStack().pop();

		if (d instanceof TextData) {
			TextData textData = (TextData) d;
			Double value = Double.parseDouble(textData.stringValue());
			context.getStack().push(new DecimalData(value));
		}
		else {
			context.getStack().push(new NullData());
		}

		return true;
	}

}
