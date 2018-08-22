package fr.ambox.assembler.procedures;

import fr.ambox.assembler.*;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.atoms.IntegerAtom;
import fr.ambox.assembler.atoms.NameAtom;
import fr.ambox.assembler.atoms.ReferenceAtom;
import fr.ambox.assembler.datas.BlockData;
import fr.ambox.assembler.datas.DictData;
import fr.ambox.assembler.datas.NameData;
import fr.ambox.assembler.exceptions.ExpectedBlockDataException;
import fr.ambox.assembler.exceptions.ExpectedDictDataException;
import fr.ambox.assembler.exceptions.ExpectedNameDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class ForKeyValueProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws FatalErrorException {
		Stack stack = context.getStack();

		Data d4 = stack.pop();
		Data d3 = stack.pop();
		Data d2 = stack.pop();
		Data d1 = stack.pop();
		
		if (!(d4 instanceof BlockData)) {
			throw new ExpectedBlockDataException();
		}
		BlockData block = (BlockData) d4;

		if (!(d3 instanceof DictData)) {
			throw new ExpectedDictDataException();
		}
		DictData source = (DictData) d3;
		
		if (!(d2 instanceof NameData)) {
			throw new ExpectedNameDataException();
		}
		NameData vname = (NameData) d2;
		String valueName = vname.getValue();
		
		if (!(d1 instanceof NameData)) {
			throw new ExpectedNameDataException();
		}
		NameData kname = (NameData) d1;
		String keyName = kname.getValue();

		AtomList code = new AtomList();
		int sourceId = context.getStore().add(d3);

		code.add(new ReferenceAtom("@" + sourceId));

		code.add(new CallAtom("blstart"));
		code.add(new NameAtom(":" + valueName));
		code.add(new CallAtom("exch"));
		code.add(new CallAtom("def"));
		code.add(new NameAtom(":" + keyName));
		code.add(new CallAtom("exch"));
		code.add(new CallAtom("def"));
		code.append(block.assemble(context));
		code.add(new CallAtom("blstop"));

		code.add(new CallAtom("for"));

		code.add(new IntegerAtom(String.valueOf(sourceId)));
		code.add(new CallAtom("free"));

		context.getBootstrap().push(new Step(code));
        return true;
	}
}
