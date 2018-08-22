package fr.ambox.assembler.procedures;

import fr.ambox.assembler.*;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.atoms.IntegerAtom;
import fr.ambox.assembler.atoms.NameAtom;
import fr.ambox.assembler.atoms.ReferenceAtom;
import fr.ambox.assembler.datas.BlockData;
import fr.ambox.assembler.datas.NameData;
import fr.ambox.assembler.datas.StringData;
import fr.ambox.assembler.datas.TableData;
import fr.ambox.assembler.exceptions.*;

public class TryCatchFinallyProcedure implements Procedure {

	private boolean hasCatch;
	private boolean hasFinally;
	
	public TryCatchFinallyProcedure(boolean hasCatch, boolean hasFinally) {
		this.hasCatch = hasCatch;
		this.hasFinally = hasFinally;
	}
	
	public TryCatchFinallyProcedure() {
		this(true, true);
	}
	
	@Override
	public boolean execute(Context context) throws FatalErrorException {
		// preparing
		Data data;

        BlockData tryBlock = null;
		BlockData finallyBlock = null;
		BlockData catchBlock = null;
		String exceptionName = null;
		
		if (this.hasFinally) {
			data = context.getStack().pop();
			if (!(data instanceof BlockData)) {
				throw new ExpectedBlockDataException();
			}
			finallyBlock = (BlockData) data;
		}


		if (this.hasCatch) {
			data = context.getStack().pop();
			if (!(data instanceof BlockData)) {
				throw new ExpectedBlockDataException();
			}
			catchBlock = (BlockData) data;
			data = context.getStack().pop();
			if (!(data instanceof NameData)) {
				throw new ExpectedNameDataException();
			}
			exceptionName = ((NameData) data).stringValue();
		}
		
		data = context.getStack().pop();
		if (!(data instanceof BlockData)) {
			throw new ExpectedBlockDataException();
		}
		tryBlock = (BlockData) data;

        AtomList code = new AtomList();

		code.append(tryBlock.assemble(context));
		code.add(new CallAtom("try"));

        if (catchBlock != null) {
            int catchBlockId = context.getStore().add(catchBlock);

            code.add(new NameAtom(":" + exceptionName));
            code.add(new ReferenceAtom("@" + catchBlockId));
            code.add(new CallAtom("catch"));

            code.add(new IntegerAtom(String.valueOf(catchBlockId)));
            code.add(new CallAtom("free"));
        }

        if (finallyBlock != null) {
            code.append(finallyBlock.assemble(context));
        }

        context.getBootstrap().push(new Step(code));
		return true;
	}
}
