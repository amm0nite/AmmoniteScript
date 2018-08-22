package fr.ambox.assembler.datas;

import fr.ambox.assembler.Data;

public final class DecimalData extends Data implements DoubleData, TextData {
	private final Double value;

	public DecimalData(double value) {
		this.value = value;
	}
	
	public double doubleValue() {
		return this.value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof DecimalData)) {
			return false;
		}
		DecimalData other = (DecimalData) obj;
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
		return Double.toString(this.value);
	}

	@Override
	public String toString() {
		return this.stringValue();
	}
}
