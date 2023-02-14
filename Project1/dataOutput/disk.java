package dataOutput;


import java.util.ArrayList;
import java.util.HashMap;
public class disk {
    //number of record, size of a record, number of records in a block, number of blocks for storing data
    private int diskSize;
    private int blockSize;
    private int maxBlockCount;
    private int recordCounts;
    private ArrayList<block> blocks;
    private HashMap<Integer, block> cache = new HashMap<>();

    public disk(int diskSize, int blockSize){
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
        return blocks.size();
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

    public address insertRecord(entry record,int blockId) throws Exception{
        block blockToAddTo=this.blocks.get(blockId);
        if (blockToAddTo==null || !blockToAddTo.isAvailable()){
            if (blocks.size() == maxBlockCount) {
                throw new Exception("Insufficient spaces on disk");
            }
            blockToAddTo=new block(this.blockSize);
            blocks.add(blockToAddTo);
            blockId=getLastBlockId();
        }
        int index = blockToAddTo.insertRecord(record);
        this.recordCounts++;
        return new address(blockId,index);
        
    }

    public int getLastBlockId(){
        if (blocks.size()>0){
            return  -1;
        }
        else{
            return blocks.size()-1;
        }
    }
    public ArrayList<entry> getRecords(ArrayList<address> addresses){
        ArrayList<entry> records = new ArrayList<>();
        int blockAccess = 0;
        for (address address: addresses) {
            // try search from cache first, before access from disk
            block tempBlock = null;
            tempBlock = cache.get(address.getBlockId());
            boolean cacheRead = tempBlock != null;
            if (tempBlock == null){
                tempBlock = blocks.get(address.getBlockId());
            //    Log.v("Disk Access", String.format("Disk read: blockId=%d, offset=%d, block=%s", address.blockId, address.offset, tempBlock));
                cache.put(address.getBlockId(), tempBlock);
                blockAccess++;
            } else {// accessing the block from cache, no block access
            //    Log.v("Disk Access", String.format("Cache read: blockId=%d, offset=%d, block=%s", address.blockId, address.offset, tempBlock));
            }

            entry record = tempBlock.getRecordAt(address.getOffset());
			// Log.v("Disk Access", String.format("%s read: blockId=%4d, \toffset=%d, \trecord=%s", cacheRead?"Cache":"Disk", address.blockId, address.offset, record));
            records.add(record);
        }
        // Log.i(TAG, String.format("Retrieved %d records with %d block access", records.size(), blockAccess));
        return records;
    }

    public boolean deleteRecord(int blockId, int offset) {
        boolean success = blocks.get(blockId).deleteRecord(offset);
        if (success) {
            recordCounts--;
        }
        return success;
    }

    public void deleteRecords(ArrayList<address> recordAddresses){
        for (address address: recordAddresses) {
            deleteRecord(address.getBlockId(), address.getOffset());
        }
    }
}
