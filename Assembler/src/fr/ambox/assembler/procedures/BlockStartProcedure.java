package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.atoms.CallAtom;

public class BlockStartProcedure implements Procedure {

	@Override
	public boolean execute(Context context) {
		if (!context.getBlockFlag()) {
			context.setBlockFlag(true);
			context.resetBlockCounter();
			context.resetBlockBuffer();
		}
		else {
			context.getBlockBuffer().add(new CallAtom("blstart"));
			context.incrementBlockCounter();
		}

		return true;
	}

}
