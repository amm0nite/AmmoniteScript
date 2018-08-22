package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.NameData;
import fr.ambox.assembler.exceptions.ExpectedIntegerDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class DeleteProcedure implements Procedure {

    @Override
    public boolean execute(Context context) throws FatalErrorException {
        Data data = context.getStack().pop();

        if (!(data instanceof NameData)) {
            throw new ExpectedIntegerDataException();
        }
        NameData name = (NameData) data;

        context.getDictStack().remove(name.stringValue());
        return true;
    }
}
