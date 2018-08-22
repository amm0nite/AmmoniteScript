package fr.ambox.assembler.procedures;

import fr.ambox.assembler.*;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.datas.BlockData;
import fr.ambox.assembler.datas.BooleanData;
import fr.ambox.assembler.exceptions.ExpectedBlockDataException;
import fr.ambox.assembler.exceptions.ExpectedBooleanDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class IfProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws FatalErrorException {
		Data d2 = context.getStack().pop();
		Data d1 = context.getStack().pop();
		
		if (!(d1 instanceof BooleanData)) {
			System.out.println("got: "+d1.getClass().getName());
			throw new ExpectedBooleanDataException(d1);
		}
		
		if (!(d2 instanceof BlockData)) {
			throw new ExpectedBlockDataException();
		}
		
		BooleanData cond = (BooleanData) d1;
		BlockData trueBlock = (BlockData) d2;
		
		if (cond.getValue()) {
            AtomList code = new AtomList();
            code.append(trueBlock.toAtomBlock());
            code.add(new CallAtom("do"));
			context.getBootstrap().push(new Step(code));
		}

		return true;
	}
}
