package fr.ambox.assembler;

import java.util.ArrayList;
import java.util.HashMap;

public class AtomListBag {
    private ArrayList<String> keys;
    private HashMap<String, AtomList> map;

    public AtomListBag() {
        this.keys = new ArrayList<String>();
        this.map = new HashMap<String, AtomList>();
    }

    public AtomList get(String key) {
        return this.map.get(key);
    }

    public AtomList get(int index) {
        return this.map.get(this.keys.get(index));
    }

    public String getKey(int index) {
        return this.keys.get(index) + "";
    }

    public int size() {
        return this.keys.size();
    }

    public boolean has(String key) {
        return this.map.containsKey(key);
    }

    public void put(String key, AtomList value) {
        while (this.has(key)) {
            key += "~";
        }
        this.keys.add(key);
        this.map.put(key, value);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (String key: this.keys) {
            sb.append(index);
            sb.append(" :: ");
            sb.append(key);
            sb.append(" => ");
            sb.append(this.map.get(key));
            sb.append("\n");
            index++;
        }
        return sb.toString();
    }
}
