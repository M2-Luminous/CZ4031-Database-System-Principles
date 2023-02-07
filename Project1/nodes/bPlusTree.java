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
        leafNode LEAF = new leafNode();
        int i;

        //get sorted lists of keys and addresses
        for(i = 0; i < maxKeys; i ++) {
            keys[i] = old.getOneKey(i);
            addresses[i] = old.getOneRecord(i);
        }

        for(i = maxKeys - 1; i >= 0; i --) {
            if(keys[i] <= key) {
                i ++;
                keys[i] = key;
                addresses[i] = address;
                break;
            }
            keys[i + 1] = keys[i];
            addresses[i + 1] = addresses[i];
        }

        //cleaning old leafnode values
        old.splitPrep();

        //putting the keys and addresses into the two leafnodes
        for(i = 0; i < leafMinKeys; i ++) {
            old.addRecord(keys[i], addresses[i]);
        }

        for(i = leafMinKeys; i < maxKeys + 1; i ++) {
            LEAF.addRecord(keys[i], addresses[i]);
        }

        //old leafnode to point to new leafnode
        //new leafnode point to next leafnode 
        LEAF.setNext(old.getNext());
        old.setNext(LEAF);

        //setting parents for LEAF
        if(old.getIsRoot()) {
            parentNode newRoot = new parentNode();
            old.setIsRoot(false);
            newRoot.setIsRoot(true);
            newRoot.addChild(old);
            newRoot.addChild(LEAF);
            root = newRoot;
            height++;
        }
        else if(old.getParent().getAllKey().size() < maxKeys) {
            old.getParent().addChild(LEAF);
        }
        else {
            splitParent(old.getParent(), LEAF);
        }

        //update node count
        nodeCount ++;
    }

    //split parent node
    public void splitParent(parentNode parent, Node child) {
        Node children[] = new Node[maxKeys + 2];
        int keys[] = new int[maxKeys + 2];
        int key = child.findSmallestKey();
        parentNode PARENT = new parentNode();

        //getting sorted lists of keys and children
        for(int i = 0; i < maxKeys + 1; i ++) {
            children[i] = parent.getChild(i);
            keys[i] = children[i].findSmallestKey();
        }

        for(int i = maxKeys; i >= 0; i --) {
            if(keys[i] <= key) {
                i++;
                keys[i] = key;
                children[i] = child;
                break;
            }

            keys[i + 1] = keys[i];
            children[i + 1] = children[i];
        }

        //cleaning old parent values
        parent.splitPrep();

        //putting children into two parent nodes
        for(int i = 0; i < parentMinKeys + 2; i ++) {
            parent.addChild(children[i]);
        }
        for(int i = parentMinKeys + 2; i < maxKeys + 2; i ++) {
            PARENT.addChild(children[i]);
        }

        //setting new parent node
        if(parent.getIsRoot()) {
            parentNode newRoot = new parentNode();
            parent.setIsRoot(false);
            newRoot.setIsRoot(true);
            newRoot.addChild(parent);
            newRoot.addChild(PARENT);
            root = newRoot;
            height ++;
        }
        else if(parent.getParent().getAllKey().size() < maxKeys) {
            parent.getParent().addChild(PARENT);
        }
        else {
            splitParent(parent.getParent(), PARENT);
        }

        //update node count
        nodeCount ++;
    }
}
