package fr.ambox.assembler.datas;

import fr.ambox.assembler.Data;

public final class NullData extends Data implements TextData {

	@Override
	public String stringValue() {
		return "null";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { 
			return false; 
		}
		if (obj == this) { 
			return true; 
		}
		if (!(obj instanceof NullData)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return 1474202476;
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
}
