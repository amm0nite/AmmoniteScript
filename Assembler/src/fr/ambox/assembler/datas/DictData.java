package fr.ambox.assembler.datas;

import fr.ambox.assembler.Data;
import fr.ambox.assembler.exceptions.DictOperationException;

public interface DictData extends LengthData {
	public Data get(String key) throws DictOperationException;
	public void put(String key, Data value) throws DictOperationException;
	public boolean has(String key) throws DictOperationException;
    public String[] keys();
}
