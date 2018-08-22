package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.NameData;
import fr.ambox.assembler.exceptions.ExpectedNameDataException;

public class DefinitionProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws ExpectedNameDataException {
		Data value = context.getStack().pop();
		Data name = context.getStack().pop();
		
		if (!(name instanceof NameData)) {
			throw new ExpectedNameDataException();
		}
		
		NameData variableName = (NameData) name;
		String key = variableName.getValue();
		context.getDictStack().put(key, value);
		return true;
	}
}
