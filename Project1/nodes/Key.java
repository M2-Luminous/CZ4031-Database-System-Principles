package nodes;

import java.util.Arrays;


//Class representing key of a node in a B+ tree
 
public class Key implements Comparable<Key> {
    
    //Primary indexed attribute (numVotes)
    private int key1;
    private Key key;

    //Secondary indexed attribute (tconst)
    private char[] key2;

    //pointer to node
    private Node node;

    //record address
    private leafNode record;

    /**
     * Construct a key
     * @param key1 primary indexed attribute
     * @param key2 secondary indexed attribute
     * @param key key
     * @param record record address
     */

    //for key comparison
    public Key(int key1, char[] key2) {
        this.key1 = key1;
        this.key2 = key2;
    }

    //for key node comparison
    public Key(Key key, Node node) {
        this.key = key;
        this.node = node;
    }

    //for key record comparison
    public Key(Key key, leafNode record) {
        this.key = key;
        this.record = record;
    }

    public int getkey1() {
        return key1;
    }

    public void setkey1(int key1) {
        this.key1 = key1;
    }

    public char[] getkey2() {
        return key2;
    }

    public void setkey2(char[] key2) {
        this.key2 = key2;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public leafNode getRecord() {
        return record;
    }

    public void setRecord(leafNode record) {
        this.record = record;
    }

    //for record comparison
    //@Override
    //public int compareTo(Key currentKey) {
    //    if (currentKey == null) return -1;
    //    return this.key.compareTo(currentKey.key);
    //}

    //for node comparison
    //@Override
    //public int compareTo(Key currentKey) {
    //    return this.key.compareTo(currentKey.key);
    //}

    @Override
    public int compareTo(Key currentKey) {
        if (currentKey == null) {
            return -1;
        } 
        if (this.key1 == currentKey.key1) {
            return Arrays.toString(this.key2).compareTo(Arrays.toString(currentKey.key2));
        }
        return Integer.compare(this.key1, currentKey.key1);
    }


}