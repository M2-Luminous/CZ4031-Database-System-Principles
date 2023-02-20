package dataOutput;

public class address {
    private int blockid;
    private int offset;

    public address(int blockid, int offset){
        // 7 bytes to represent block id
        this.blockid=blockid;
        // 1 byte to represent offset since offset is less than 16
        this.offset=offset;
    }

    public void setBlockId(int blockid){
        this.blockid=blockid;
    }
    public void setOffset(int offset){
        this.offset=offset;
    }

    public int getBlockId(){
        return this.blockid;
    }
    public int getOffset(){
        return this.offset;
    }

}
