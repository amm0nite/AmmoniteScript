package fr.ambox.assembler.exceptions;

import fr.ambox.assembler.Data;

public class ExpectedBooleanDataException extends FatalErrorException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 878581237983330215L;

	private Data data;

	public ExpectedBooleanDataException(Data d) {
		super();
		this.data = d;
	}
}
