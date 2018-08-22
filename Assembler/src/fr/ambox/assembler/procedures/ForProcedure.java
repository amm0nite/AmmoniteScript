package fr.ambox.assembler.procedures;

import fr.ambox.assembler.*;
import fr.ambox.assembler.atoms.*;
import fr.ambox.assembler.datas.ArrayData;
import fr.ambox.assembler.datas.BlockData;
import fr.ambox.assembler.datas.DictData;
import fr.ambox.assembler.datas.StringData;
import fr.ambox.assembler.exceptions.*;

public class ForProcedure implements Procedure {

	@Override
	public boolean execute(Context context) throws FatalErrorException {
		Data d2 = context.getStack().pop();
		Data d1 = context.getStack().pop();
		
		if (!(d2 instanceof BlockData)) {
			throw new ExpectedBlockDataException();
		}
		BlockData block = (BlockData) d2;

		if (!(d1 instanceof DictData)) {
            throw new ExpectedDictDataException();
		}
		DictData source = (DictData) d1;

        ArrayData keyArray = new ArrayData();
        String[] keys = source.keys();
        for (String key: keys) {
            keyArray.append(new StringData(key));
        }
        int keyArrayId = context.getStore().add(keyArray);

        String counterName = context.getDictStack().generateIdentifier();
        int sourceId = context.getStore().add(d1);
        int length = source.getLength();
        AtomList code = new AtomList();

        // setup
        code.add(new NameAtom(":" + counterName));
        code.add(new IntegerAtom("0"));
        code.add(new CallAtom("def"));

        // condition
        code.add(new CallAtom("blstart"));
        code.add(new VariableAtom("$" + counterName));
        code.add(new IntegerAtom(String.valueOf(length)));
        code.add(new CallAtom("lt"));
        code.add(new CallAtom("blstop"));

        // body start
        code.add(new CallAtom("blstart"));

        // fill stack
        code.add(new ReferenceAtom("@" + keyArrayId));
        code.add(new VariableAtom("$" + counterName));
        code.add(new CallAtom("get"));
        code.add(new CallAtom("dup"));
        code.add(new ReferenceAtom("@" + sourceId));
        code.add(new CallAtom("exch"));
        code.add(new CallAtom("get"));

        // body
        code.append(block.toAtomBlock());

        // increment
        code.add(new CallAtom("do"));
        code.add(new VariableAtom("$" + counterName));
        code.add(new IntegerAtom("1"));
        code.add(new CallAtom("add"));
        code.add(new NameAtom(":" + counterName));
        code.add(new CallAtom("exch"));
        code.add(new CallAtom("def"));

        // body stop
        code.add(new CallAtom("blstop"));

        // while
        code.add(new CallAtom("while"));

        // cleanup
        code.add(new NameAtom(":" + counterName));
        code.add(new CallAtom("delete"));
        code.add(new IntegerAtom(String.valueOf(keyArrayId)));
        code.add(new CallAtom("free"));
        code.add(new IntegerAtom(String.valueOf(sourceId)));
        code.add(new CallAtom("free"));

        context.getBootstrap().push(new Step(code));
        return true;
	}
}
