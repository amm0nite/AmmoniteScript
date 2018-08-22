package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.*;
import fr.ambox.assembler.exceptions.*;

public class FunctionCreationProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws FatalErrorException {
		Data d = context.getStack().pop();

        // Container
		if (!(d instanceof TableData)) {
			throw new ExpectedTableDataException();
		}
		TableData dict = (TableData) d;

		// Extract args
		Data argsData = dict.get("args");
		if (argsData == null) {
			throw new ExpectedTablePropertyException();
		}
		if (!(argsData instanceof ArrayData)) {
			throw new ExpectedArrayDataException();
		}
		ArrayData args = (ArrayData) argsData;

		// Check args
		for (Data argData: args) {
            if (!(argData instanceof NameData)) {
                throw new ExpectedNameDataException();
            }
        }

		// Extract code
		Data codeData = dict.get("code");
		if (codeData == null) {
			throw new ExpectedTablePropertyException();
		}
		if (!(codeData instanceof BlockData)) {
			throw new ExpectedBlockDataException();
		}
		BlockData code = (BlockData) codeData;

        // Closure locals
		TableData locals = context.getDictStack().peek();

        // Create function
		FunctionData function = new FunctionData(args, code, locals);
		context.getStack().push(function);
		return true;
	}
}
