package Project1.dataOutput;

import java.util.Arrays;

public class block {
    private int max_num_records;
    public int current_num_records;
    private entry[] all_records;

    public block(int size){
        this.max_num_records=size/entry.get_storage();
        this.current_num_records=0;
        this.all_records=new entry[this.max_num_records];
    }

    public boolean checkinsert(){
        return this.max_num_records>=this.current_num_records;
    }

    public int insertrecord(entry record){
        if (this.checkinsert()){
            for(int i=0;i<this.all_records.length;i++){
                if (this.all_records[i]==null){
                    this.all_records[i]=record;
                    this.current_num_records++;
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean deleterecord(int index){
        if (this.all_records[index]!=null){
            this.all_records[index]=null;
            this.current_num_records--;
            return true;
        }
        return false;
    }

    public entry getrecord(int index){
        return this.all_records[index];
    }

    public entry getrecordat(int index){
        return this.all_records[index];
    }

    public void printblock(){
        System.out.println("Printing block:");
        System.out.printf("Current number of records: %d. All records:%n",this.current_num_records);
        for (int i=0; i<current_num_records;i++){
            this.all_records[i].printentry();
        }
        System.out.println("Finished printing block");
    }
}
