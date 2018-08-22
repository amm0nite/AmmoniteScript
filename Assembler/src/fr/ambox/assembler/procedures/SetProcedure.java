package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.Stack;
import fr.ambox.assembler.datas.DictData;
import fr.ambox.assembler.datas.FunctionData;
import fr.ambox.assembler.datas.TextData;
import fr.ambox.assembler.exceptions.*;

public class SetProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws FatalErrorException {
        Stack stack = context.getStack();

		Data d3 = stack.pop();
		Data d2 = stack.pop();
		Data d1 = stack.pop();

		if (!(d1 instanceof DictData)) {
			throw new ExpectedDictDataException();
		}
        Data tableData = d1;
		DictData table = (DictData) d1;

		if (!(d2 instanceof TextData)) {
			throw new ExpectedTextDataException();
		}
		TextData textData = (TextData) d2;
		String key = textData.stringValue();

		Data value = d3;
		if (value instanceof FunctionData) {
			FunctionData function = (FunctionData) value;
			function.setParent(tableData);
			function.setName(key);
		}

		try {
            table.put(key, value);
            stack.push(tableData);
        }
        catch (DictOperationException e) {
            throw new DictOperationErrorException(e);
        }

		return true;
	}

}
