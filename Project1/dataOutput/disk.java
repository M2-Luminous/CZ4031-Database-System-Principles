package dataOutput;

import java.util.ArrayList;
import java.util.HashMap;

public class disk {
    // number of record, size of a record, number of records in a block, number of
    // blocks for storing data
    private int diskSize; //Disk Size
    private int blockSize; //Block Size
    private int recordCounts; //Number of records
    private ArrayList<block> blocks; //All the blocks
    private HashMap<Integer, block> cache = new HashMap<>();

    public disk(int diskSize, int blockSize) {
        this.diskSize = diskSize;
        this.blockSize = blockSize;
        this.recordCounts = 0;
        this.blocks = new ArrayList<>();

    }
    public ArrayList<block> getBlocks(){
        return this.blocks;
    }

    /*
     * Get the maximum number of blocks that can exist in the storage
     */
    public int getMaxBlockCount(){
        return this.diskSize / this.blockSize;
    }

    /*
     * Get the total number of blocks exist in the storage
     */
    public int getBlocksCount() {
        return this.blocks.size();
    }

    /*
     * Get the total number of records exist in the storage
     */
    public int getRecordCounts() {
        return recordCounts;
    }

    /*
     * Get the used size of storage
     */
    public int getUsedSize() {
        return getBlocksCount() * blockSize;
    }

    public address insertRecord(Record record, int blockId) throws Exception {
        block blockToAddTo=null;
        if (blockId>-1){
            blockToAddTo = this.blocks.get(blockId);
        }

        if (blockToAddTo == null || !blockToAddTo.isAvailable()) {
            if (this.blocks.size() == this.diskSize / this.blockSize) {
                throw new Exception("Insufficient spaces on disk");
            }
            blockToAddTo = new block(this.blockSize);
            this.blocks.add(blockToAddTo);
            blockId = getLastBlockId();
        }
        int index = blockToAddTo.insertRecord(record);
        this.recordCounts++;
        return new address(blockId, index);

    }

    /*
     * Get the last block id
     */
    public int getLastBlockId() {
        if (this.blocks.size() <= 0) {
            return -1;
        } else {
            return this.blocks.size() - 1;
        }
    }

    public ArrayList<Record> getRecords(ArrayList<address> addresses) {
        ArrayList<Record> records = new ArrayList<>();
        for (address address : addresses) {
            block tempBlock = null;
            tempBlock = cache.get(address.getBlockId());
            if (tempBlock == null) {
                tempBlock = this.blocks.get(address.getBlockId());
                cache.put(address.getBlockId(), tempBlock);
            }

            Record record = tempBlock.getRecordAt(address.getOffset());
            records.add(record);
        }
        return records;
    }

    public boolean deleteRecord(int blockId, int offset) {
        boolean success = this.blocks.get(blockId).deleteRecord(offset);
        if (success) {
            recordCounts--;
        }
        return success;
    }

    public void deleteRecords(ArrayList<address> recordAddresses) {
        for (address address : recordAddresses) {
            deleteRecord(address.getBlockId(), address.getOffset());
        }
    }

    /*
     * Get the size of storage
     */
    public int getDiskSize() {
        return this.diskSize;
    }
}
