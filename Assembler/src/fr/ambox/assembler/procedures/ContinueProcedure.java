package fr.ambox.assembler.procedures;

import fr.ambox.assembler.Context;
import fr.ambox.assembler.Procedure;
import fr.ambox.assembler.SkipState;
import fr.ambox.assembler.exceptions.FatalErrorException;

public class ContinueProcedure implements Procedure {

    @Override
    public boolean execute(Context context) throws FatalErrorException {
        context.setSkipState(SkipState.Continue);
        return true;
    }
}
