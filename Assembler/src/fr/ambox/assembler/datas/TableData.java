package fr.ambox.assembler.datas;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import fr.ambox.assembler.Data;
import fr.ambox.assembler.exceptions.DictOperationException;

public class TableData extends Data implements DictData {
	
	private LinkedHashMap<String, Data> map;

	public TableData() {
		this.map = new LinkedHashMap<String, Data>();
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
		if (!(data instanceof TableData)) {
			return false;
		}
		TableData other = (TableData) data;
		if (this.map.size() != other.map.size()) {
			return false;
		}
		for (Entry<String, Data> entry: this.map.entrySet()) {
            if (!entry.getValue().isLike(other.map.get(entry.getKey()))) {
                return false;
            }
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		String prefix = "";
		for (Entry<String, Data> e: this.map.entrySet()) {
			sb.append(prefix);
			prefix = ", ";
			sb.append(e.getKey());
			sb.append(' ');
			sb.append(e.getValue().toString());
		}
		sb.append(")");
		return sb.toString();
	}

    public void safePut(String key, Data value) {
		this.map.put(key, value);
    }
	
	@Override
	public void put(String key, Data value) throws DictOperationException {
        this.safePut(key, value);
	}

	@Override
	public int getLength() {
		return this.map.size();
	}

	public LinkedHashMap<String, Data> getMap() {
		return this.map;
	}

	@Override
	public Data get(String key) {
		return this.map.get(key);
	}

	@Override
    public boolean has(String key) {
        return this.map.containsKey(key);
    }

	@Override
	public String[] keys() {
		int i = 0;
		String[] result = new String[this.map.size()];
		for (String key: this.map.keySet()) {
			result[i] = key;
			i++;
		}
		return result;
	}

	public Iterable<Entry<String,Data>> entrySet() {
		return this.map.entrySet();
	}

    public void remove(String key) {
        this.map.remove(key);
    }

    public void mimic(TableData table) {
    	this.map = table.map;
    }
}
