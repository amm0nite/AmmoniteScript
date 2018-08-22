package fr.ambox.assembler.exceptions;

import fr.ambox.assembler.Data;

public class WrapDataException extends FatalErrorException {

	private Data data;

	public WrapDataException(Data data) {
		this.data = data;
	}

    @Override
    public Data toData() {
        return this.data;
    }
}
