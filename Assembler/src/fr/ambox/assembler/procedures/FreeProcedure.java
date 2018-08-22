package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.IntegerData;
import fr.ambox.assembler.exceptions.ExpectedIntegerDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class FreeProcedure implements Procedure {

    @Override
    public boolean execute(Context context) throws FatalErrorException {
        Data data = context.getStack().pop();

        if (!(data instanceof IntegerData)) {
            throw new ExpectedIntegerDataException();
        }
        IntegerData id = (IntegerData) data;

        context.getStore().remove(id.intValue());
        return true;
    }
}
