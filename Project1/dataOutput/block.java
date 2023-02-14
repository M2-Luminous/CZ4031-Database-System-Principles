package dataOutput;

import java.util.Arrays;

public class block {
    private int maxNumRecords;
    public int currentNumRecords;
    private entry[] allRecords;

    public block(int size){
        this.maxNumRecords=size/entry.getStorage();
        this.currentNumRecords=0;
        this.allRecords=new entry[this.maxNumRecords];
    }

    public boolean isAvailable(){
        return this.maxNumRecords>=this.currentNumRecords;
    }

    public int insertRecord(entry record){
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

    public entry getRecord(int index){
        return this.allRecords[index];
    }

    public entry getRecordAt(int index){
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
}
