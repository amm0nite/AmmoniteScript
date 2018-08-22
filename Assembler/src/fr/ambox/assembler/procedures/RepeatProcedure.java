package fr.ambox.assembler.procedures;

import fr.ambox.assembler.*;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.atoms.IntegerAtom;
import fr.ambox.assembler.atoms.NameAtom;
import fr.ambox.assembler.atoms.VariableAtom;
import fr.ambox.assembler.datas.BlockData;
import fr.ambox.assembler.datas.IntegerData;
import fr.ambox.assembler.exceptions.ExpectedBlockDataException;
import fr.ambox.assembler.exceptions.ExpectedIntegerDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class RepeatProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws FatalErrorException {
		Data d2 = context.getStack().pop();
		Data d1 = context.getStack().pop();
		
		if (!(d1 instanceof IntegerData)) {
			throw new ExpectedIntegerDataException();
		}
		
		if (!(d2 instanceof BlockData)) {
			throw new ExpectedBlockDataException();
		}
		
		IntegerData lengthData = (IntegerData) d1;
		BlockData block = (BlockData) d2;

        String counterName = context.getDictStack().generateIdentifier();
		AtomList code = new AtomList();

        // setup
        code.add(new NameAtom(":" + counterName));
        code.add(new IntegerAtom(lengthData.stringValue()));
        code.add(new CallAtom("def"));

        // condition
        code.add(new CallAtom("blstart"));
        code.add(new VariableAtom("$" + counterName));
        code.add(new IntegerAtom("0"));
        code.add(new CallAtom("gt"));
        code.add(new CallAtom("blstop"));

		// code
        code.add(new CallAtom("blstart"));
        code.append(block.toAtomBlock());
        code.add(new CallAtom("do"));
        code.add(new VariableAtom("$" + counterName));
        code.add(new IntegerAtom("1"));
        code.add(new CallAtom("sub"));
        code.add(new NameAtom(":" + counterName));
        code.add(new CallAtom("exch"));
        code.add(new CallAtom("def"));
        code.add(new CallAtom("blstop"));

		// while
        code.add(new CallAtom("while"));
        code.add(new NameAtom(":" + counterName));
        code.add(new CallAtom("delete"));

        context.getBootstrap().push(new Step(code));
        return true;
	}
}
