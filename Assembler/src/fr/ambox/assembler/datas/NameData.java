package fr.ambox.assembler.datas;

import fr.ambox.assembler.Data;

public final class NameData extends Data implements TextData {
	
	private final String value;
	
	public NameData(String value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) { 
			return false; 
		}
		if (obj == this) { 
			return true; 
		}
		if (!(obj instanceof NameData)) {
			return false;
		}
		NameData other = (NameData) obj;
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

	public String getValue() {
		return this.value;
	}

	@Override
	public String stringValue() {
		return this.value;
	}
	
	public String getTokenString() {
		return ":"+this.value;
	}
	
	@Override
	public String toString() {
		return this.getTokenString();
	}
}
