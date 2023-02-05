package Project1.nodes;

//import app.storage.Address;
//import app.util.Log;
import java.util.ArrayList;

public class leafNode extends Node{
    public static final String TAG = "Node.L";

    private ArrayList<Address> records;
    private leafNode next;

    public leafNode() {
        super();
        records = new ArrayList<Address>();
        setLeaf(true);
        setNext(null);
    }

    //record at arraylist
    public ArrayList<Address> getRecords() {
        return records;
    }

    //record at index
    public Address getRecord(int index) {
        return records.get(index);
    }
}
