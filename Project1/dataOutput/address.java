package dataOutput;

public class Address {
    private int blockid;
    private int offset;

    public Address(int blockid, int offset) {
        this.blockid = blockid;
        this.offset = offset;
    }

    public void setBlockId(int blockid) {
        this.blockid = blockid;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getBlockId() {
        return this.blockid;
    }

    public int getOffset() {
        return this.offset;
    }

}
