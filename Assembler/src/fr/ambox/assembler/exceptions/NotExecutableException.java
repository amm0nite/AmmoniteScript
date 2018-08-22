package fr.ambox.assembler.exceptions;

import fr.ambox.assembler.Data;

public class NotExecutableException extends FatalErrorException {

	private Data hint;

	public NotExecutableException(Data d) {
		this.hint = d;
	}

	@Override
	public String getMessage() {
		return this.hint.toString();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 447319615491192274L;

}
