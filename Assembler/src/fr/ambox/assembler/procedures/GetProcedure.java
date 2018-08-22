package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.Stack;
import fr.ambox.assembler.datas.DictData;
import fr.ambox.assembler.datas.FunctionData;
import fr.ambox.assembler.datas.NullData;
import fr.ambox.assembler.datas.TextData;
import fr.ambox.assembler.exceptions.*;

public class GetProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws FatalErrorException {
        Stack stack = context.getStack();

		Data d2 = stack.pop();
		Data d1 = stack.pop();

        if (!(d2 instanceof TextData)) {
            throw new ExpectedTextDataException();
        }

        TextData textData = (TextData) d2;
		String index = textData.stringValue();

		if (!(d1 instanceof DictData)) {
			throw new ExpectedDictDataException();
		}
        Data tableData = d1;
		DictData table = (DictData) d1;

		try {
            Data value = table.get(index);
            if (value == null) {
                stack.push(new NullData());
            } else {
                stack.push(value);
            }
        }
        catch (DictOperationException e) {
            throw new DictOperationErrorException(e);
        }

        return true;
	}
}
