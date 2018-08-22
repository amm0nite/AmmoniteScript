package fr.ambox.assembler;

import java.util.ArrayDeque;

public class Stack extends Data {
    private ArrayDeque<Data> stack;

    public Stack() {
        this.stack = new ArrayDeque<Data>();
    }

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    public void push(Data data) {
        this.stack.push(data);
    }

    public Data pop() {
        return this.stack.pop();
    }

    public void clear() {
        this.stack.clear();
    }

    public int size() {
        return this.stack.size();
    }

    public void add(Data data) {
        this.stack.add(data);
    }

    public Data[] toArray() {
        return this.stack.toArray(new Data[this.stack.size()]);
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
        if (!(data instanceof Stack)) {
            return false;
        }
        Stack other = (Stack) data;
        if (other.stack.size() != this.stack.size()) {
            return false;
        }
        Data[] otherContent = other.toArray();
        Data[] thisContent = this.toArray();
        for (int i=0; i<thisContent.length; i++) {
            if (otherContent[i].isLike(thisContent[i])) {
                return false;
            }
        }
        return true;
    }
}
