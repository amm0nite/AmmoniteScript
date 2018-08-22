package fr.ambox.assembler.exceptions;

public class UndefinedException extends FatalErrorException {

    private String name;
    private String container;

	public UndefinedException(String name, String container) {
		this.name = name;
		this.container = container;
	}
	
	@Override
	public String getMessage() {
		return this.name + " in " + this.container;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1131746077788190282L;

}
