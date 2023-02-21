package dataOutput;

public class address {
    //Block ID of the record
    private int blockid;
    //Index of the record in the block
    private byte offset;

    public address(int blockid, int offset){
        // 7 bytes to represent block id
        this.blockid = blockid;
        // 1 byte to represent offset since offset is less than 16
        this.offset= (byte) offset;
    }

    public void setBlockId(int blockid){
        this.blockid=blockid;
    }
    public void setOffset(int offset){
        this.offset= (byte) offset;
    }

    public int getBlockId(){
        return this.blockid;
    }
    public int getOffset(){
        return (int) this.offset;
    }

}
