package fr.ambox.assembler.exceptions;

public class WrapException extends FatalErrorException {
    private Exception exception;

    public WrapException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String getMessage() {
        String message = this.exception.getMessage();
        if (message == null) {
            return exception.toString();
        }
        return message;
    }
}
