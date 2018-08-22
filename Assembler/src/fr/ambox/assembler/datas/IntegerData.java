package fr.ambox.assembler.datas;

import fr.ambox.assembler.Data;

public final class IntegerData extends Data implements DoubleData, TextData {
	private final Long value;
	
	public IntegerData(long value) {
		this.value = value;
	}
	
	public long longValue() {
		return this.value;
	}
	
	public int intValue() {
		return this.value.intValue();
	}
	
	public double doubleValue() {
		return (double) this.value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) { 
			return false; 
		}
		if (obj == this) { 
			return true; 
		}
		if (!(obj instanceof IntegerData)) {
			return false;
		}
		IntegerData other = (IntegerData) obj;
		return this.value.equals(other.value);
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public boolean isEqualTo(Data data) {
		if (data == null) {
			return false;
		}
		if (data == this) {
			return true;
		}
		if (!(data instanceof DoubleData)) {
			return false;
		}
		DoubleData other = (DoubleData) data;
		return this.doubleValue() == other.doubleValue();
	}

	@Override
	public boolean isLike(Data data) {
		return this.isEqualTo(data);
	}

	@Override
	public String stringValue() {
		return Long.toString(this.value);
	}

	@Override
	public String toString() {
		return this.stringValue();
	}
}
