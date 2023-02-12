package dataOutput;

import app.utility.utility;
import app.utility.log;

import java.util.*;

public class disk {
    private static final String TAG = "Disk";
    int diskSize;
    int maxBlockCount;
    int blockSize;
    int recordCounts;
    ArrayList<block> blocks;

    public disk(int diskSize, int blockSize){
        this.diskSize = diskSize;
        this.blockSize = blockSize;
        this.maxBlockCount = diskSize / blockSize;
        this.blocks = new ArrayList<>();
        this.recordCounts = 0;
        log();
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


    /**
     * insert the records into first available block for record insertion, however, it can be expensive!!!
     * @param record inserting record
     * @return address of record being inserted
     * @throws Exception
     */
    public address insertRecord(Record Record) throws Exception {
        int blockId = getFirstAvailableBlockId();
        return insertRecordAt(blockId, Record);
    }

    /**
     * Attempt to insert record into last block. if last block is not available, record will be inserted into newly created block.
     * noted that on  no checking of availability will be done on prev blocks
     * @param record inserting record
     * @return address of record being inserted
     * @throws Exception
     */
    public address appendRecord(Record record) throws Exception{
        int blockId = getLastBlockId();
        return insertRecordAt(blockId, record);
    }

    private address insertRecordAt(int blockId, Record record) throws Exception {
        block block = null;
        if (blockId>=0){
            block = getBlockAt(blockId);
        }

        // block is not available/not exist, try to create a new block to insert the record
        if (block == null || !block.isAvailable()) {
            if (blocks.size() == maxBlockCount) {
                throw new Exception("Insufficient spaces on disk");
            }
            block = new block(blockSize);
            blocks.add(block);
            blockId = getLastBlockId();
        }
        int offset = block.insertRecord(record);
        recordCounts++;
//        Log.v(String.format("Record inserted at %d-%d", blockId, offset));
        return new address(blockId, offset);
    }


    public int getFirstAvailableBlockId(){
        int blockId = -1;
        for(int i=0; i<blocks.size(); i++){
            if(blocks.get(i).isAvailable()){
                blockId = i;
                break;
            }
        }
        return blockId;
    }

    public int getLastBlockId(){
        return blocks.size()>0? blocks.size()-1:-1;
    }

    public block getBlockAt(int blockId){
        return blocks.get(blockId);
    }

    public Record getRecordAt(int blockId, int offset){
        return getBlockAt(blockId).getRecordAt(offset);
    }

    public Record getRecordAt(address address){
        return getRecordAt(address.getBlockId(), address.getOffset());
    }

    public ArrayList<Record> getRecords(ArrayList<address> addresses ){
        //addresses.sort(Comparator.comparingInt(Address::getBlockId));
        HashMap<Integer, block> cache = new HashMap<>();
        ArrayList<Record> records = new ArrayList<>();
        int blockAccess = 0;
        block tempBlock = null;
        for (address address: addresses) {
            // try search from cache first, before access from disk
            tempBlock = cache.get(address.getBlockId());
            boolean cacheRead = tempBlock != null;
            if (tempBlock == null){
                tempBlock = getBlockAt(address.getBlockId());
//                Log.v("Disk Access", String.format("Disk read: blockId=%d, offset=%d, block=%s", address.blockId, address.offset, tempBlock));
                cache.put(address.getBlockId(), tempBlock);
                blockAccess++;
            } else {// accessing the block from cache, no block access
//                Log.v("Disk Access", String.format("Cache read: blockId=%d, offset=%d, block=%s", address.blockId, address.offset, tempBlock));
            }

            Record record = tempBlock.getRecordAt(address.getOffset());
			log.v("Disk Access", String.format("%s read: blockId=%4d, \toffset=%d, \trecord=%s", cacheRead?"Cache":"Disk", address.blockId, address.offset, record));
            records.add( record );
        }
        log.i(TAG, String.format("Retrieved %d records with %d block access", records.size(), blockAccess));
        return records;
    }




    public boolean deleteRecordAt(int blockId, int offset) {
        boolean success = getBlockAt(blockId).deleteRecordAt(offset);
        if (success) {
            recordCounts--;
        }
        return success;
    }

    public void deleteRecords(ArrayList<address> recordAddresses){
        for (address address: recordAddresses) {
            deleteRecordAt(address.getBlockId(), address.getOffset());
        }
    }


    // debugs only
    public void log(){
        log.d(TAG, String.format("disk size = %s / %s", utility.formatFileSize(getUsedSize()), utility.formatFileSize(diskSize) ));
        log.d(TAG, String.format("block size = %s", utility.formatFileSize(blockSize)));
        log.d(TAG, String.format("blocks = %,d / %,d", blocks.size(), maxBlockCount));
        log.d(TAG, String.format("records = %,d", recordCounts));
    }
}
