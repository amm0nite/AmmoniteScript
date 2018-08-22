package fr.ambox.assembler.exceptions;

public class DictOperationErrorException extends FatalErrorException {

    private DictOperationException exception;

    public DictOperationErrorException(DictOperationException e) {
        this.exception = e;
    }

    public DictOperationException getException() {
        return this.exception;
    }
}
