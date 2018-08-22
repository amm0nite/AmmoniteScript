package fr.ambox.assembler.procedures;

import fr.ambox.assembler.*;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.datas.BlockData;
import fr.ambox.assembler.datas.BooleanData;
import fr.ambox.assembler.exceptions.ExpectedBlockDataException;
import fr.ambox.assembler.exceptions.ExpectedBooleanDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class IfElseProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws FatalErrorException {
        Stack stack = context.getStack();

		Data d3 = stack.pop();
		Data d2 = stack.pop();
		Data d1 = stack.pop();
		
		if (!(d1 instanceof BooleanData)) {
			throw new ExpectedBooleanDataException(d1);
		}
		
		if (!(d2 instanceof BlockData && d3 instanceof BlockData)) {
			throw new ExpectedBlockDataException();
		}
		
		BooleanData cond = (BooleanData) d1;
		BlockData trueBlock = (BlockData) d2;
		BlockData falseBlock = (BlockData) d3;
		
		if (cond.getValue()) {
            AtomList code = new AtomList();
            code.append(trueBlock.toAtomBlock());
            code.add(new CallAtom("do"));
            context.getBootstrap().push(new Step(code));
		}
		else {
            AtomList code = new AtomList();
            code.append(falseBlock.toAtomBlock());
            code.add(new CallAtom("do"));
            context.getBootstrap().push(new Step(code));
		}

		return true;
	}
}
