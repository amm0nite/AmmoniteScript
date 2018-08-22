package fr.ambox.assembler;

import fr.ambox.assembler.datas.DictData;
import fr.ambox.assembler.datas.TableData;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;

/**
 * Stack of dictionaries
 * Responsible for scope
 */
public class DictStack extends Data implements DictData, Iterable<TableData> {

    private ArrayDeque<TableData> stack;

    public DictStack() {
        this.stack = new ArrayDeque<TableData>();
    }

    @Override
    public void put(String key, Data value) {
        TableData dict = this.findSource(key);
        if (dict == null) {
            dict = this.stack.peek();
            if (dict == null) {
                this.push();
                dict = this.stack.peek();
            }
        }

        dict.safePut(key, value);
    }

    @Override
    public Data get(String key) {
        TableData dict = this.findSource(key);
        if (dict != null) {
            return dict.get(key);
        }
        return null;
    }

    @Override
    public boolean has(String key) {
        return (this.findSource(key) != null);
    }

    @Override
    public String[] keys() {
        return this.peek().keys();
    }

    private TableData findSource(String key) {
        for (TableData dict : this.stack) {
            if (dict.has(key)) {
                return dict;
            }
        }
        return null;
    }

    public void push() {
        this.stack.push(new TableData());
    }

    public TableData pop() {
        return this.stack.pop();
    }

    public TableData peek() {
        return this.stack.peek();
    }

    public String generateIdentifier() {
        int i = 0;
        String result;
        do {
            i++;
            result = "_" + i;
        } while (this.has(result));
        return result;
    }

    @Override
    public int getLength() {
        return this.peek().getLength();
    }

    public void remove(String key) {
        TableData dict = this.findSource(key);
        if (dict != null) {
            dict.remove(key);
        }
    }

    @Override
    public Iterator<TableData> iterator() {
        return this.stack.iterator();
    }

    public void add(TableData dict) {
        this.stack.add(dict);
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
        if (!(data instanceof DictStack)) {
            return false;
        }
        DictStack other = (DictStack) data;
        if (this.stack.size() != other.stack.size()) {
            return false;
        }
        TableData[] values = this.stack.toArray(new TableData[0]);
        TableData[] otherValues = other.stack.toArray(new TableData[0]);
        for (int i=0; i<values.length; i++) {
            if (!values[i].isEqualTo(otherValues[i])) {
                return false;
            }
        }
        return true;
    }

    public void mimic(DictStack completeDictStack) {
        this.stack = completeDictStack.stack;
    }
}
