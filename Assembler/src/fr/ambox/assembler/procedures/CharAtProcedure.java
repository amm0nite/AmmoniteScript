package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.IntegerData;
import fr.ambox.assembler.datas.StringData;
import fr.ambox.assembler.datas.TextData;
import fr.ambox.assembler.exceptions.ExpectedIntegerDataException;
import fr.ambox.assembler.exceptions.ExpectedTextDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class CharAtProcedure implements Procedure {
    @Override
    public boolean execute(Context context) throws FatalErrorException {
        Data d2 = context.getStack().pop();
        Data d1 = context.getStack().pop();

        if (!(d1 instanceof TextData)) {
            throw new ExpectedTextDataException();
        }
        TextData textData = (TextData) d1;

        if (!(d2 instanceof IntegerData)) {
            throw new ExpectedIntegerDataException();
        }
        IntegerData integerData = (IntegerData) d2;

        String result = String.valueOf(textData.stringValue().charAt(integerData.intValue()));
        context.getStack().push(new StringData(result));
        return true;
    }
}
