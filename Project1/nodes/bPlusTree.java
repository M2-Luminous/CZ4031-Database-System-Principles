package nodes;

import dataOutput.address;
import app.utility.log;
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
        
        log.i(TAG, "initial : blockSize = " + blockSize + ", maxKey = " + maxKeys);
        log.i(TAG, "minKeys : parent = " + parentMinKeys + ", leaf = " + leafMinKeys);

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
    public void insert(int key, address address) {
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
    public void insertToLeaf(leafNode leaf, int key, address address) {
        if(leaf.getAllKey().size() < maxKeys) {
            leaf.addRecord(key, address);
        }
        else {
            splitLeaf(leaf, key, address);
        }
    }

    //split a full leaf node
    public void splitLeaf(leafNode old, int key, address address) {
        int keys[] = new int[maxKeys + 1];
        address addresses[] = new address[maxKeys + 1];
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

        //cleaning old parent nodes
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

    //delete a key's record
    public void deleteKey(int key) {
        ArrayList<Integer> keys;
        leafNode leaf;

        //search remaining record of target node
        while(getRecordsWithKey(key, false).size() != 0) {
            leaf = searchLeafNode(key);
            keys = leaf.getAllKey();

            //deleting one record
            for(int i = 0; i < keys.size(); i++) {
                if(keys.get(i) == key) {
                    leaf.deleteOneRecord(i);

                    //if leaf node is not a root then update tree
                    if(!leaf.getIsRoot()) {
                        resetLeaf(leaf);
                    }
                    break;
                }
            }
        }
        log.d("deletion", "number of nodes deleted = " + deleteCount);
        
        //update node count
        nodeCount -= deleteCount;
        
        treeStats();
    }

    //reset leaf node
    public void resetLeaf(leafNode node) {
        if(node.getAllKey().size() >= leafMinKeys) {
            resetParent(node.getParent());
            return;
        }

        leafNode former = (leafNode) node.getParent().getFormer(node);
        leafNode latter = (leafNode) node.getParent().getLatter(node);
        int space = leafMinKeys - node.getAllKey().size();
        int formerSpare = 0;
        int latterSpare = 0;
        parentNode PARENT;

        //getting number of keys that before the nodes can spare
        if(former != null){
            formerSpare += former.getAllKey().size() - leafMinKeys;
        }

         //getting number of keys that after the nodes can spare
         if(latter != null){
            latterSpare += latter.getAllKey().size() - leafMinKeys;
        }

        //if merge
        if(space > formerSpare + latterSpare) {

            //if node has former nodes and latter nodes
            if(former != null && latter != null) {
                //insert maximum records into former node
                for(int i = 0; i < maxKeys - (formerSpare + leafMinKeys); i ++) {
                    former.addRecord(node.getOneKey(i), node.getOneRecord(i));
                }
                //insert the remaining records into latter node
                for(int i = maxKeys - (formerSpare + leafMinKeys); i < node.getAllKey().size(); i ++) {
                    latter.addRecord(node.getOneKey(i), node.getOneRecord(i));
                }
            }
            //if node has latter node only
            else if(former == null) {
                for(int i = maxKeys - (formerSpare + leafMinKeys); i < node.getAllKey().size(); i ++) {
                    latter.addRecord(node.getOneKey(i), node.getOneRecord(i));
                }
            }
            //if node has former node only
            else if(latter == null) {
                for(int i = 0; i < maxKeys - (formerSpare + leafMinKeys); i ++) {
                    former.addRecord(node.getOneKey(i), node.getOneRecord(i));
                }
            }

            PARENT = node.getParent();

            //look for the former node if they don't share same parent
            if(former == null) {
                if(!PARENT.getIsRoot()) {
                    former = searchLeafNode(PARENT.findSmallestKey() - 1);
                }
            }
            //update link
            former.setNext(node.getNext());
            //delete old node
            node.deleteNode();
            //update count
            deleteCount ++;
        }
        //if borrow keys
        else{
            if(former != null & latter != null) {
                //find the last few keys from former node that can be spared
                for(int i = 0; i < formerSpare; i ++) {
                    node.addRecord(former.getOneKey(former.getAllKey().size() - 1 - i), former.getOneRecord(former.getAllKey().size()-1 -i));
                    former.deleteOneRecord(former.getAllKey().size() - 1 - i);;
                }
                //find the rest keys from latter node
                for(int i = formerSpare, j = 0; i < space; i ++, j ++) {
                    node.addRecord(latter.getOneKey(j), latter.getOneRecord(j));
                    latter.deleteOneRecord(j);
                }
            }
            else if(former == null) {
                //add them all from latter node
                for(int i = 0; i < space; i ++) {
                    node.addRecord(latter.getOneKey(i), latter.getOneRecord(i));
                    latter.deleteOneRecord(i);
                }
            }
            else if(latter == null) {
                //add them all from former node
                for(int i = 0; i < space; i ++) {
                    node.addRecord(former.getOneKey(i), former.getOneRecord(i));
                    former.deleteOneRecord(former.getAllKey().size() - 1 - i);
                }
            }
            PARENT = node.getParent();
        }
        //update parents
        resetParent(PARENT);
    }

    public void resetParent(parentNode parent) {
        //if node is root itself
        if(parent.getIsRoot()) {
            //if root has at least 2 children
            if(parent.getChildren().size() > 1) {
                Node child = parent.getChild(0);
                parent.deleteOneChild(child);
                parent.addChild(child);
                return;
            }
            //if root has only 1 chld
            else {
                parent.getChild(0).setIsRoot(true);
                root = parent.getChild(0);
                parent.deleteNode();
                //update count
                deleteCount ++;
                //update height
                height -- ;
                return;
            }
        }

        parentNode former = (parentNode) parent.getParent().getFormer(parent);
        parentNode latter = (parentNode) parent.getParent().getLatter(parent);
        int space = parentMinKeys - parent.getAllKey().size();
        int formerSpare = 0;
        int latterSpare = 0;
        parentNode PARENT;

        if(former != null) {
            formerSpare += former.getAllKey().size() - parentMinKeys;
        }
        if(latter != null) {
            latterSpare += latter.getAllKey().size() - parentMinKeys;
        }

        //if merge
        if(space > formerSpare + latterSpare) {

            //if node has former nodes and latter nodes
            if(former != null && latter != null) {
                //insert maximum records into former node
                for(int i = 0; i < maxKeys - (formerSpare + parentMinKeys) + 1 && i < parent.getChildren().size(); i ++) {
                    former.addChild(parent.getChild(i));
                }
                //insert the remaining records into latter node
                for(int i = maxKeys - (formerSpare + parentMinKeys) + 1; i < parent.getChildren().size(); i ++) {
                    latter.addChild(parent.getChild(i));
                }
            }
            //if node has latter node only
            else if(former == null) {
                for(int i = 0; i < parent.getChildren().size(); i ++) {
                    latter.addChild(parent.getChild(i));
                }
            }
            //if node has former node only
            else if(latter == null) {
                for(int i = 0; i < parent.getChildren().size(); i ++) {
                    former.addChild(parent.getChild(i));
                }
            }

            PARENT = parent.getParent();
            parent.deleteNode();
            //update count
            deleteCount ++;
        }
        else{
            if(former != null & latter != null) {
                //find the last few keys from former node that can be spared
                for(int i = 0; i < formerSpare && i < space; i ++) {
                    parent.addChild(former.getChild(former.getChildren().size() - 1) , 0);
                    former.deleteOneChild(former.getChild(former.getChildren().size() - 1));
                }
                //find the rest keys from latter node
                for(int i = formerSpare; i < space; i ++) {
                    parent.addChild(latter.getChild(0));
                    latter.deleteOneChild(latter.getChild(0));
                }
            }
            else if(former == null) {
                //add them all from latter node
                for(int i = 0; i < space; i ++) {
                    parent.addChild(latter.getChild(0));
                    latter.deleteOneChild(latter.getChild(0));
                }
            }
            else if(latter == null) {
                //add them all from former node
                for(int i = 0; i < space; i ++) {
                    parent.addChild(former.getChild(former.getChildren().size() - 1 - i) , 0);
                    former.deleteOneChild(former.getChild(former.getChildren().size() - 1 - i));
                }
            }
            PARENT = parent.getParent();
        }
        //update parents
        resetParent(PARENT);
    }

    public ArrayList<address> getRecordsWithKey(int key) {
        return getRecordsWithKey(key, true);
    }

    public ArrayList<address> getRecordsWithKey(int key, boolean isVer) {
        ArrayList<address> result = new ArrayList<>();
        int block = 1;
        int sibling = 0;
        if(isVer) {
            log.d("B+Tree.keySearch" , "[Node Access] Access root node");
        }
        Node node = root;
        parentNode PARENT;
        //search for leaf node with key
        while(!node.getIsLeaf()) {
            PARENT = (parentNode) node;
            for(int i = 0; i < PARENT.getAllKey().size(); i++) {
                if(key <= PARENT.getOneKey(i)) {
                    if(isVer) {
                        log.v("B+Tree.keySearch" , node.toString());
                        log.d("B+Tree.keySearch" , String.format("[Node Access] follow pointer [%d]: key(%d)<=curKey(%d)", i, key, PARENT.getOneKey(i)));
                    }
                    node = PARENT.getChild(i);
                    block ++;
                    break;
                }
                if(i == PARENT.getAllKey().size() - 1) {
                    if(isVer) {
                        log.v("B+Tree.keySearch", node.toString());
                        log.d("B+Tree.keySearch",String.format("[Node Access] follow pointer [%d+1]: last key and key(%d)>curKey(%d)", i, key, PARENT.getOneKey(i) ));
                    }
                    node = PARENT.getChild(i + 1);
                    block ++;
                    break;
                }
            }
        }
        //after leaf node is discoverd, find out all records of same keys
        leafNode LEAF = (leafNode) node;
        boolean check = false;
        while(!check && LEAF != null) {
            //find same keys in leaf node
            for(int i = 0; i < LEAF.getAllKey().size(); i ++) {
                //if founded, add into result list
                if(LEAF.getOneKey(i) == key) {
                    result.add(LEAF.getOneRecord(i));
                    continue;
                }
                //if current key > searching key, then stop searching
                if(LEAF.getOneKey(i) > key) {
                    check = true;
                    break;
                }
            }
            if(!check) {
                //check whether sibling node has same key's record or not
                if(LEAF.getNext() != null) {
                    LEAF = LEAF.getNext();
                    block ++;
                    sibling ++;
                } 
                else {
                    break;
                }
            }
        }
        if(sibling > 0) {
            if(isVer) {
                log.d("B+Tree.keySearch", "[Node Access] " + sibling + " sibling node access");
            }
        }
        if(isVer) {
            log.i("B+Tree.keySearch", String.format("input(%d): %d records found with %d node access", key, result.size(), block));
        }
        return result;
    }

    public void treeStats() {
        ArrayList<Integer> rootKey = new ArrayList<Integer>();
        ArrayList<Integer> headKey = new ArrayList<Integer>();
        parentNode ROOT = (parentNode) root;
        Node head = ROOT.getChild(0);

        for(int i = 0; i < root.getAllKey().size(); i++) {
            rootKey.add(root.getOneKey(i));
        }
        for(int i = 0; i < head.getAllKey().size(); i ++) {
            headKey.add(head.getOneKey(i));
        }

        log.d("treeStats", "n = " + maxKeys + ", number of nodes = " + nodeCount + ", height = " + height);
        log.d("rootContents", "root node contents = " + rootKey);
        log.d("firstContents", "first child contents = " + headKey);
    }

    public ArrayList<address> getRecordsWithKeyInRange(int min, int max) {
        return getRecordsWithKeyInRange(min, max, true);
    }

    public ArrayList<address> getRecordsWithKeyInRange(int min, int max, boolean isVer) {
        ArrayList<address> result = new ArrayList<>();
        int nodeAccess = 1;
        int siblingAccess = 0;
        if(isVer) {
            log.d("B+Tree.rangeSearch", "[Node Access] Access root node");
        }
        Node node = root;
        parentNode PARENT;
        //search for leaf node with key
        while(!node.getIsLeaf()) {
            PARENT = (parentNode) node;
            for(int i = 0; i < PARENT.getAllKey().size(); i ++) {
                if(min <= PARENT.getOneKey(i)) {
                    if(isVer) {
                        log.v("B+Tree.rangeSearch", node.toString());
                        log.d("B+Tree.rangeSearch", String.format("[Node Access] follow pointer [%d]: min(%d)<=curKey(%d)", i, min, PARENT.getOneKey(i)));
                    }
                    node = PARENT.getChild(i);
                    nodeAccess ++;
                    break;
                }
                if(i == PARENT.getAllKey().size() - 1) {
                    if(!isVer) {
                        if (isVer) {
                            log.v("B+Tree.rangeSearch", node.toString());
                            log.d("B+Tree.rangeSearch", String.format("[Node Access] follow pointer [%d+1]: last key and min(%d)>curKey(%d)", i, min, PARENT.getOneKey(i)));
                        }
                    }
                    node = PARENT.getChild(i + 1);
                    nodeAccess ++;
                    break;
                }
            }
        }
        //after leaf node is found, find all records with same key
        leafNode LEAF = (leafNode) node;
        boolean check = false;
        while(!check && LEAF != null) {
            //search for same keys and add them into result list
            for(int i = 0; i < LEAF.getAllKey().size(); i ++) {
                if(LEAF.getOneKey(i) >= min && LEAF.getOneKey(i) <= max) {
                    result.add(LEAF.getOneRecord(i));
                    continue;
                }
                if(LEAF.getOneKey(i) > max) {
                    check = true;
                    break;
                }
            }
            if(!check) {
                //check whether sibling node has remaining records of same key or not
                if(LEAF.getNext() != null) {
                    LEAF = (leafNode) LEAF.getNext();
                    nodeAccess ++;
                    siblingAccess ++; 
                }
                else {
                    break;
                }
            }
        }
        if (siblingAccess > 0){
            if (isVer) {
                log.d("B+Tree.rangeSearch", "[Node Access] " + siblingAccess + " sibling node access");
            }
        }
        if (isVer) {
            log.i("B+Tree.rangeSearch", String.format("input(%d, %d): %d records found with %d node access", min, max, result.size(), nodeAccess));
        }
        return result;
    }

    public ArrayList<address> removeRecordsWithKey() {
        return null;
    }

    public void logStructure(){
        logStructure(0 , Integer.MAX_VALUE , root);
    }

    public void logStructure(int maxLevel){
        logStructure(0 , maxLevel, root);
    }

    private void logStructure(int level, int maxLevel, Node node) {
        if(node == null) {
            node = root;
        }
        if(level > maxLevel) {
            return;
        }

        System.out.print("h = " + level + " ; ");
        node.logStructure();
        if(node.getIsLeaf()) {
            return;
        }
        parentNode PARENT = (parentNode) node;
        for(Node child : PARENT.getChildren()) {
            logStructure(level + 1, maxLevel, child);
        } 
    }
}
