package dataOutput;


import java.util.ArrayList;
import java.util.HashMap;
public class Disk {
    //number of record, size of a record, number of records in a block, number of blocks for storing data
    private int diskSize;
    private int blockSize;
    private int maxBlockCount;
    private int recordCounts;
    private ArrayList<Block> blocks;
    private HashMap<Integer, Block> cache = new HashMap<>();

    public Disk(int diskSize, int blockSize){
        this.diskSize=diskSize;
        this.blockSize=blockSize;
        this.maxBlockCount=diskSize/blockSize;
        this.recordCounts=0;
        this.blocks=new ArrayList<>();

    }

    /**
     * Get the total number of blocks exist in the storage
     * @return
     */
    public int getBlocksCount(){
        return this.blocks.size();
    }

    /**
     * Get the total number of records exist in the storage
     * @return
     */
    public int getRecordCounts(){
        return recordCounts;
    }
    /**
     * Get the used size of storage
     * @return
     */
    public int getUsedSize(){
        return getBlocksCount() * blockSize;
    }

    public int getAvailableBlockId(){
        for (int i=0;i<this.maxBlockCount;i++){
            if (!this.blocks.get(i).isAvailable()){
                continue;
            }
            return i;
        }
        return -1;
    }

    public Address insertRecord(Record record,int blockId) throws Exception{
        Block blockToAddTo=this.blocks.get(blockId);
        if (blockToAddTo==null || !blockToAddTo.isAvailable()){
            if (this.blocks.size() == maxBlockCount) {
                throw new Exception("Insufficient spaces on disk");
            }
            blockToAddTo=new Block(this.blockSize);
            this.blocks.add(blockToAddTo);
            blockId=getLastBlockId();
        }
        int index = blockToAddTo.insertRecord(record);
        this.recordCounts++;
        return new Address(blockId,index);
        
    }

    public int getLastBlockId(){
        if (this.blocks.size()>0){
            return  -1;
        }
        else{
            return this.blocks.size()-1;
        }
    }
    public ArrayList<Record> getRecords(ArrayList<Address> addresses){
        ArrayList<Record> records = new ArrayList<>();
        // int blockAccess = 0;
        for (Address address: addresses) {
            // try search from cache first, before access from disk
            Block tempBlock = null;
            tempBlock = cache.get(address.getBlockId());
            // boolean cacheRead = tempBlock != null;
            if (tempBlock == null){
                tempBlock = this.blocks.get(address.getBlockId());
            //    Log.v("Disk Access", String.format("Disk read: blockId=%d, offset=%d, block=%s", address.blockId, address.offset, tempBlock));
                cache.put(address.getBlockId(), tempBlock);
                // blockAccess++;
            } else {// accessing the block from cache, no block access
            //    Log.v("Disk Access", String.format("Cache read: blockId=%d, offset=%d, block=%s", address.blockId, address.offset, tempBlock));
            }

            Record record = tempBlock.getRecordAt(address.getOffset());
			// Log.v("Disk Access", String.format("%s read: blockId=%4d, \toffset=%d, \trecord=%s", cacheRead?"Cache":"Disk", address.blockId, address.offset, record));
            records.add(record);
        }
        // Log.i(TAG, String.format("Retrieved %d records with %d block access", records.size(), blockAccess));
        return records;
    }

    public boolean deleteRecord(int blockId, int offset) {
        boolean success = this.blocks.get(blockId).deleteRecord(offset);
        if (success) {
            recordCounts--;
        }
        return success;
    }

    public void deleteRecords(ArrayList<Address> recordAddresses){
        for (Address address: recordAddresses) {
            deleteRecord(address.getBlockId(), address.getOffset());
        }
    }
}
