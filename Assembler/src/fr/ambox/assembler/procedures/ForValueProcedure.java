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

public class ForValueProcedure implements Procedure {
	
	@Override
	public boolean execute(Context context) throws FatalErrorException {
		Data d3 = context.getStack().pop();
		Data d2 = context.getStack().pop();
		Data d1 = context.getStack().pop();

		if (!(d3 instanceof BlockData)) {
			throw new ExpectedBlockDataException();
		}
		BlockData block = (BlockData) d3;

		if (!(d2 instanceof DictData)) {
			throw new ExpectedDictDataException();
		}
		DictData source = (DictData) d2;
		
		if (!(d1 instanceof NameData)) {
			throw new ExpectedNameDataException();
		}
		NameData vname = (NameData) d1;
		String valueName = vname.getValue();

        AtomList code = new AtomList();
        int sourceId = context.getStore().add(d2);

        code.add(new ReferenceAtom("@" + sourceId));
        code.add(new CallAtom("blstart"));
        code.add(new NameAtom(":" + valueName));
        code.add(new CallAtom("exch"));
        code.add(new CallAtom("def"));
        code.add(new CallAtom("pop"));
        code.append(block.assemble(context));
        code.add(new CallAtom("blstop"));
        code.add(new CallAtom("for"));

        code.add(new IntegerAtom(String.valueOf(sourceId)));
        code.add(new CallAtom("free"));

        context.getBootstrap().push(new Step(code));
        return true;
	}

}
