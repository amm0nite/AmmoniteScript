package fr.ambox.assembler;

import java.util.ArrayList;
import java.util.Iterator;

public class AtomList implements Iterable<Atom> {
    private ArrayList<Atom> list;

    public AtomList() {
        this.list = new ArrayList<Atom>();
    }

    @Override
    public Iterator<Atom> iterator() {
        return this.list.iterator();
    }

    public void add(Atom atom) {
        this.list.add(atom);
    }

    public void addFirst(Atom atom) {
        this.list.add(0, atom);
    }

    public void prepend(AtomList atoms) {
        ArrayList<Atom> newList = new ArrayList<Atom>();
        newList.addAll(atoms.list);
        newList.addAll(this.list);
        this.list = newList;
    }

    public void append(AtomList atoms) {
        this.list.addAll(atoms.list);
    }

    public int size() {
        return this.list.size();
    }

    public Atom get(int ip) {
        return this.list.get(ip);
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public String toCode() {
        StringBuilder code = new StringBuilder();
        for (Atom atom: this) {
            code.append(" ");
            code.append(atom.toTokenString());
        }
        return code.toString().trim();
    }

    public String toString() {
        return "<AtomList>" + this.toCode() + "</AtomList>";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AtomList)) {
            return false;
        }
        AtomList other = (AtomList) obj;
        return this.list.equals(other.list);
    }
}
