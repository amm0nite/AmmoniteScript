package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.TableData;

public class TableProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		context.getStack().push(new TableData());
		return true;
	}
}
