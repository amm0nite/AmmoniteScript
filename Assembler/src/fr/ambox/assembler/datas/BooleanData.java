package fr.ambox.assembler.datas;

import fr.ambox.assembler.Data;

public final class BooleanData extends Data implements TextData {

	private final Boolean value;

	public BooleanData(boolean value) {
		this.value = value;
	}

	@Override
	public String stringValue() {
		return (this.value) ? "true" : "false";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof BooleanData)) {
			return false;
		}
		BooleanData other = (BooleanData) obj;
		return this.value.equals(other.value);
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

    @Override
    public boolean isEqualTo(Data data) {
        return this.equals(data);
    }

    @Override
	public boolean isLike(Data data) {
		return this.equals(data);
	}

    @Override
	public String toString() {
		return this.stringValue();
	}

	public boolean getValue() {
		return this.value;
	}
}
