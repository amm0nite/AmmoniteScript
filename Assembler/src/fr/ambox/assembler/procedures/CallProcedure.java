package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.Trace;
import fr.ambox.assembler.datas.TextData;
import fr.ambox.assembler.exceptions.ExpectedTextDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class CallProcedure implements Procedure {
    @Override
    public boolean execute(Context context) throws FatalErrorException {
        Data d2 = context.getStack().pop();
        Data d1 = context.getStack().pop();

        if (!(d1 instanceof TextData)) {
            throw new ExpectedTextDataException();
        }
        TextData name = (TextData) d1;

        context.getCallStack().push(new Trace(name.stringValue(), d2));
        return true;
    }
}
