package fr.ambox.assembler;

import fr.ambox.assembler.exceptions.FatalErrorException;

public interface Procedure {
	public boolean execute(Context context) throws FatalErrorException;
}
