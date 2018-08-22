package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Data;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.datas.TableData;
import fr.ambox.assembler.exceptions.ExpectedTableDataException;
import fr.ambox.assembler.exceptions.FatalErrorException;

import java.util.Map;

public class ImportProcedure implements Procedure {

    @Override
    public boolean execute(Context context) throws FatalErrorException {
        Data data = context.getStack().pop();

        if (!(data instanceof TableData)) {
            throw new ExpectedTableDataException();
        }
        TableData importTable = (TableData) data;

        for (Map.Entry<String, Data> entry: importTable.entrySet()) {
            context.getDictStack().put(entry.getKey(), entry.getValue());
        }

        return true;
    }
}
