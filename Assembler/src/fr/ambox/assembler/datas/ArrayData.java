package fr.ambox.assembler.datas;

import fr.ambox.assembler.Data;

import java.util.*;

public class ArrayData extends ListData implements Iterable<Data> {

    private ArrayList<Data> values;

    public ArrayData() {
        this.values = new ArrayList<Data>();
    }

    @Override
    public boolean isEqualTo(Data data) {
        return (data != null && data == this);
    }

    @Override
    public boolean isLike(Data data) {
        if (data == null) {
            return false;
        }
        if (data == this) {
            return true;
        }
        if (!(data instanceof ArrayData)) {
            return false;
        }
        ArrayData other = (ArrayData) data;
        if (this.values.size() != other.values.size()) {
            return false;
        }
        for (int i=0; i<this.values.size(); i++) {
            if (!this.values.get(i).isLike(other.get(i))) {
                return false;
            }
        }
        return true;
    }

    public Data get(int index) {
        return this.values.get(index);
    }

    @Override
    public void put(int index, Data value) {
        this.values.add(index, value);
    }

    @Override
    public void append(Data value) {
        this.values.add(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        String prefix = "";
        for (Data data: this) {
            sb.append(prefix);
            sb.append(data.toString());
            prefix = ", ";
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public int getLength() {
        return this.values.size();
    }

    @Override
    public String[] keys() {
        String[] result = new String[this.values.size()];
        for (int i=0; i<this.values.size(); i++) {
            result[i] = String.valueOf(i);
        }
        return result;
    }

    @Override
    public Iterator<Data> iterator() {
        return this.values.iterator();
    }

    public void mimic(ArrayData array) {
        this.values = array.values;
    }
}
