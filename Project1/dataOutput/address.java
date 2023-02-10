package Project1.dataOutput;

public class address {
    private block block_data;
    private int block_ID;

    public address(int block_ID){
        this.block_ID=block_ID;
        this.block_data=new block(200);
    }
    public block get_block(){
        return this.block_data;
    }
    public int get_block_ID(){
        return this.block_ID;
    }
}
