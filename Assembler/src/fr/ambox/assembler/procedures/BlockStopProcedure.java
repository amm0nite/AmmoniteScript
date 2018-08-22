package fr.ambox.assembler.procedures;

import fr.ambox.assembler.AtomList;
import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.datas.BlockData;
import fr.ambox.assembler.exceptions.BlockCreationFailedExcepetion;

public class BlockStopProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws BlockCreationFailedExcepetion {
		if (context.getBlockFlag()) {
			if (context.getBlockCounter() == 0) {
				AtomList atoms = new AtomList();
				atoms.append(context.getBlockBuffer());
				context.getStack().push(new BlockData(atoms));
				context.setBlockFlag(false);
			}
			else {
				context.getBlockBuffer().add(new CallAtom("blstop"));
				context.decrementBlockCounter();
			}
		}
		else {
			throw new BlockCreationFailedExcepetion();
		}

		return true;
	}

}
