package fr.ambox.assembler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public abstract class Store<T> implements Iterable<Entry<Integer, T>> {

	private int index;
	private HashMap<Integer, T> map;
	private HashMap<T, Integer> mirror;
	
	public Store() {
		this.index = 0;
		this.map = new HashMap<Integer, T>();
        this.mirror = new HashMap<T, Integer>();
	}
	
	public int add(T element) {
        // Hashcode is used to find existing element
        Integer existingId = this.mirror.get(element);
        if (existingId != null) {
			return existingId;
        }

		this.index++;
		this.map.put(this.index, element);
        this.mirror.put(element, this.index);

		return this.index;
	}

	public T get(int id) {
		return this.map.get(id);
	}

    public void put(Integer id, T element) {
        this.map.put(id, element);
        this.mirror.put(element, id);
        if (this.index < id) {
            this.index = id;
        }
    }

	public void remove(int id) {
		T element = this.map.get(id);
		if (element != null) {
			this.map.remove(id);
			Integer mirrorId = this.mirror.get(element);
			if (mirrorId != null) {
				this.mirror.remove(element);
			}
		}
	}

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public void extend(Store<T> store) {
        this.index = store.index;
        this.map = store.map;
        this.mirror = store.mirror;
    }

    @Override
    public Iterator<Entry<Integer, T>> iterator() {
        return this.map.entrySet().iterator();
    }

    public Set<Integer> keySet() {
        return this.map.keySet();
    }
}
