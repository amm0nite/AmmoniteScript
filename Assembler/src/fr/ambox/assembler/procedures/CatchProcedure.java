package fr.ambox.assembler.procedures;

import fr.ambox.assembler.*;
import fr.ambox.assembler.atoms.CallAtom;
import fr.ambox.assembler.atoms.IntegerAtom;
import fr.ambox.assembler.atoms.NameAtom;
import fr.ambox.assembler.atoms.ReferenceAtom;
import fr.ambox.assembler.datas.BlockData;
import fr.ambox.assembler.datas.NameData;
import fr.ambox.assembler.datas.NullData;
import fr.ambox.assembler.exceptions.ExpectedBlockDataException;
import fr.ambox.assembler.exceptions.ExpectedNameDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class CatchProcedure implements Procedure {

    @Override
    public boolean execute(Context context) throws FatalErrorException {
        Data d2 = context.getStack().pop();
        Data d1 = context.getStack().pop();

        if (!(d1 instanceof NameData)) {
            throw new ExpectedNameDataException();
        }
        NameData name = (NameData) d1;

        BlockData catchBlock;
        if (d2 instanceof NullData) {
            catchBlock = null;
        }
        else if (d2 instanceof BlockData) {
            catchBlock = (BlockData) d2;
        }
        else {
            throw new ExpectedBlockDataException();
        }

        if (context.getError() != null && catchBlock != null) {
            Data errorData = context.getError().toData();
            int errorDataId = context.getStore().add(errorData);

            AtomList code = new AtomList();
            code.add(new NameAtom(":" + name.stringValue()));
            code.add(new ReferenceAtom("@" + errorDataId));
            code.add(new CallAtom("def"));
            code.append(catchBlock.assemble(context));
            code.add(new IntegerAtom(String.valueOf(errorDataId)));
            code.add(new CallAtom("free"));

            context.getBootstrap().push(new Step(code));
            context.setError(null);
        }

        return true;
    }
}
