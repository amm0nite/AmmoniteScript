package fr.ambox.lexer;

public class NoTokenException extends Exception {

	private int index;
	private String near;

	public NoTokenException(int index, String near) {
		this.index = index;
        this.near = near;
	}

    public int getIndex() {
        return index;
    }

	@Override
	public String getMessage() {
		return "No acceptable token ("+this.index+") near " + near.substring(0, Math.min(10, near.length()));
	}
}
