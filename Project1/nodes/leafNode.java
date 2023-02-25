package nodes;

import dataOutput.address;
import java.util.ArrayList;

public class leafNode extends Node{
    public static final String TAG = "Node.L";

    private ArrayList<address> records;
    private leafNode next;

    public leafNode() {
        super();
        records = new ArrayList<address>();
        setIsLeaf(true);
        setNext(null);
    }

    //record at arraylist
    public ArrayList<address> getAllRecord() {
        return records;
    }

    //record at index
    public address getOneRecord(int index) {
        return records.get(index);
    }

    //add record
    public int addRecord(int key, address address) {
        if(this.getAllRecord().size() == 0) {
            this.records.add(address);
            this.addKey(key);
            return 0;
        }

        int index = super.addKey(key);
        records.add(address);

        for(int i = records.size() - 2; i >= index; i--){
            records.set(i + 1, records.get(i));
        }

        records.set(index,address);

        return index;
    }

    //get next leaf node
    public leafNode getNext() {
        return next;
    }

    //get next leaf node
    public void setNext(leafNode brother) {
        next = brother;
    }

    //splitting leafNode
    public void splitPrep() {
        deleteAllKey();
        records = new ArrayList<address>();
    }

    //deleting one record
    public void deleteOneRecord(int index) {
        deleteOneKey(index);
        records.remove(index);
    }

    //deleting all records
    public void deleteAllRecord() {
        records = new ArrayList<address>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < getAllKey().size(); i ++) {
            if (i > 0) {
                sb.append(", ");
            }
             sb.append(String.format("%d:{%d=>%s}", i, getOneKey(i) , getOneRecord(i)));
        }
        sb.append("]");
        return sb.toString();
    }    
}
