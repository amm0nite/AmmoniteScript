package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class TryProcedure implements Procedure {

    @Override
    public boolean execute(Context context) throws FatalErrorException {
        context.resetSkipState();
        return true;
    }
}
