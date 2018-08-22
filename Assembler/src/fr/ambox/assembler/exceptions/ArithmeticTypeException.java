package fr.ambox.assembler.exceptions;

public class ArithmeticTypeException extends FatalErrorException {

    private String className1;
    private String className2;

	public ArithmeticTypeException(String className1, String className2) {
		super();
        this.className1 = className1;
        this.className2 = className2;
	}

	public String getMessage() {
        return this.className1 + ", " + this.className2;
    }
}
