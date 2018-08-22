package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class ExitProcedure implements Procedure {
    @Override
    public boolean execute(Context context) throws FatalErrorException {
        context.setStopFlag(true);
        return true;
    }
}
