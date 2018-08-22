package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.TextData;
import fr.ambox.assembler.exceptions.ExpectedTextDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;

import java.io.PrintStream;

public class PrintProcedure implements Procedure {
    @Override
    public boolean execute(Context context) throws FatalErrorException {
        Data data = context.getStack().pop();
        if (!(data instanceof TextData)) {
            throw new ExpectedTextDataException();
        }

        TextData textData = (TextData) data;
        PrintStream out = new PrintStream(context.getStdout());
        out.println(textData.stringValue());
        return true;
    }
}
