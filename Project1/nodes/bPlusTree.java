package nodes;

//import app.storage.Address;
//import app.util.Log;
import java.util.ArrayList;




public class bPlusTree {
    private static final int SIZE_POINTER = 6;
    private static final int SIZE_KEY = 4;
    private static final String TAG = "bplustree";

    int maxKeys;
    int parentMinKeys;
    int leafMinKeys;
    Node root;
    int height;
    int nodeCount;
    int deleteCount;

    public bPlusTree(int blockSize){
        maxKeys = (blockSize - SIZE_POINTER) / (SIZE_KEY + SIZE_POINTER);     //maximum number of keys available
        parentMinKeys = (int) Math.floor(maxKeys/2);                           //divides 2 and rounds down 
        leafMinKeys = (int) Math.floor((maxKeys+1)/2);
        
        Log.i(TAG, "initial : blockSize = " + blockSize + ", maxKey = " + maxKeys);
        Log.i(TAG, "minKeys : parent = " + parentMinKeys + ", leaf = " + leafMinKeys);

        root = createFirst();
        nodeCount = 0;
        deleteCount = 0;
    }

    //create first node
    public leafNode createFirst() {
        leafNode newRoot = new leafNode();
        newRoot.setIsRoot(true);
        height = 1;
        nodeCount = 1;
        return newRoot;
    }

    //insert a record into b plus tree
    public void insert(int key, Address address) {
        this.insertToLeaf(this.searchLeafNode(key), key, address);
    }

    //search inserted leafnode for record
    public leafNode searchLeafNode(int key) {
        if(this.root.getIsLeaf()) {
            return (leafNode) root;
        }

        parentNode parent = (parentNode) root;

        //searching first level parent
        ArrayList<Integer> keys;
        while(!parent.getChild(0).getIsLeaf()) {
            keys = parent.getAllKey();
            for(int i = keys.size() - 1; i >= 0; i --) {
                if(keys.get(i) <= key) {
                    parent = (parentNode) parent.getChild(i + 1);
                    break;
                }
                else if(i == 0) {
                    parent = (parentNode) parent.getChild(0);
                }
            }
        }

        //searching correct leaf
        keys = parent.getAllKey();
        for(int i = keys.size() - 1; i >= 0; i --) {
            if(keys.get(i) <= key) {
                return (leafNode) parent.getChild(i + 1);
            }
        }
        return (leafNode) parent.getChild(0);
    }

    //insert record to leaf node
    public void insertToLeaf(leafNode leaf, int key, Address address) {
        if(leaf.getAllKey().size() < maxKeys) {
            leaf.addRecord(key, address);
        }
        else {
            splitLeaf(leaf, key, address);
        }
    }

    //split a full leaf node
    public void splitLeaf(leafNode old, int key, Address address) {
        int keys[] = new int[maxKeys + 1];
        Address addresses[] = new Address[maxKeys + 1];
        leafNode leaves = new leafNode();
        int i;

        //get sorted lists of keys and addresses
        for(i = 0; i< maxKeys; i++) {
            keys[i] = old.getOneKey(i);
            addresses[i] = old.getOneRecord(i);
        }
    }
}
