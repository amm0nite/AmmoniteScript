package fr.ambox.assembler;

public class Limits {
    private long atomCountLimit;
    private long bytesWrittenLimit;
    private long memsizeLimit;

    public Limits() {
        this.atomCountLimit = (long) Math.pow(2, 16);
        this.bytesWrittenLimit = (long) Math.pow(2, 16);
        this.memsizeLimit = (long) Math.pow(2, 16);
    }

    public long getAtomCountLimit() {
        return this.atomCountLimit;
    }

    public long getBytesWrittenLimit() {
        return this.bytesWrittenLimit;
    }

    public long getMemsizeLimit() {
        return this.memsizeLimit;
    }

    public void setAtomCountLimit(long atomCountLimit) {
        this.atomCountLimit = atomCountLimit;
    }

    public void setBytesWrittenLimit(long bytesWrittenLimit) {
        this.bytesWrittenLimit = bytesWrittenLimit;
    }

    public void setMemsizeLimit(long memsizeLimit) {
        this.memsizeLimit = memsizeLimit;
    }
}
