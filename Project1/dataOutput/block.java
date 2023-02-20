package dataOutput;

import java.util.Arrays;

public class block {
    //maxNumRecords needs to be in header as it shows if the block is available. 1 bytes used as it is an byte
    private byte maxNumRecords;
    //currentNumRecords needs to be in header as it shows if the block is available. 1 bytes used as it is an byte
    public byte currentNumRecords;
    //Records that will be saved in the code
    private Record[] allRecords;

    public block(int size){
        this.maxNumRecords=(byte) ((size-1)/Record.getStorage());
        this.currentNumRecords=0;
        this.allRecords=new Record[this.maxNumRecords];
    }

    public boolean isAvailable(){
        return this.maxNumRecords>this.currentNumRecords;
    }

    public int insertRecord(Record record){
        if (this.isAvailable()){
            for(int i=0;i<this.allRecords.length;i++){
                if (this.allRecords[i]==null){
                    this.allRecords[i]=record;
                    this.currentNumRecords++;
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean deleteRecord(int index){
        if (this.allRecords[index]!=null){
            this.allRecords[index]=null;
            this.currentNumRecords--;
            return true;
        }
        return false;
    }

    public Record getRecord(int index){
        return this.allRecords[index];
    }

    public Record getRecordAt(int index){
        return this.allRecords[index];
    }

    public void printBlock(){
        System.out.println("Printing block:");
        System.out.printf("Current number of records: %d. All records:%n",this.currentNumRecords);
        for (int i=0; i<currentNumRecords;i++){
            this.allRecords[i].printEntry();
        }
        System.out.println("Finished printing block");
    }

    public int getCurrentNoOfRecords(){
        return this.currentNumRecords;
    }
    
}
