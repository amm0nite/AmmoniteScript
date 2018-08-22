package fr.ambox.assembler.procedures;

import fr.ambox.assembler.*;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.datas.BlockData;
import fr.ambox.assembler.exceptions.ExpectedBlockDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class LoopProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws FatalErrorException {
		Data d = context.getStack().pop();
		
		if (!(d instanceof BlockData)) {
			throw new ExpectedBlockDataException();
		}
		
		BlockData block = (BlockData) d;

		AtomList atoms = new AtomList();

		// condition
		atoms.add(new CallAtom("blstart"));
		atoms.add(new CallAtom("true"));
		atoms.add(new CallAtom("blstop"));

		// code
		atoms.add(new CallAtom("blstart"));
		atoms.append(block.toAtomBlock());
		atoms.add(new CallAtom("do"));
		atoms.add(new CallAtom("blstop"));

		// while
		atoms.add(new CallAtom("while"));

        context.getBootstrap().push(new Step(atoms));
		return true;
	}
}
