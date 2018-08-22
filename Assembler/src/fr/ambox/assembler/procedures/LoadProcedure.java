package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.NameData;
import fr.ambox.assembler.exceptions.ExpectedNameDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;
import fr.ambox.assembler.exceptions.UndefinedException;

public class LoadProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws FatalErrorException {
		Data d = context.getStack().pop();
		
		if (!(d instanceof NameData)) {
			throw new ExpectedNameDataException();
		}
		
		NameData name = (NameData) d;
		Data data = context.getDictStack().get(name.getValue());
		
		if (data == null) {
			throw new UndefinedException(name.getValue(), "dictStack");
		}
		
		context.getStack().push(data);
		return true;
	}
}
