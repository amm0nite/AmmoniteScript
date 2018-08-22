package fr.ambox.assembler;

public abstract class Data {
    public String toString() {
        return "<Data>" + this.getClass() + "</Data>";
    }

    /**
     * isEqualTo is needed for equality in the runtime
     *
     * equals is reserved to reference equality (in the point of view of the runtime)
     * that is why two StringData are equals like they had the same Java reference because
     * we want immutable data (StringData, IntegerData ...) to have equivalent equals and isEqualTo
     * mutable data (TableData, ArrayData ...) have a classic equals based on Java reference
     * this is good to know for the inner working of the lexicon
     *
     * isEqualTo is internal value based like isLike for immutables
     * isEqualTo is Java reference based like equals for mutables
     */
    abstract public boolean isEqualTo(Data data);

    /**
     * isLike is deep value based equality
     * it is mostly used in unit tests
     */
    abstract public boolean isLike(Data data);
}
