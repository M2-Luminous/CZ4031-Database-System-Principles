package nodes;

import java.util.Arrays;


//Class representing key of a node in a B+ tree
 
public class Key implements Comparable<Key> {

    
    //Primary indexed attribute (numVotes)
     
    private int key1;

    
    //Secondary indexed attribute (tconst)
    
    private char[] key2;

    /**
     * Construct a key
     * @param key1 primary indexed attribute
     * @param key2 secondary indexed attribute
     */
    public Key(int key1, char[] key2) {
        this.key1 = key1;
        this.key2 = key2;
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

    @Override
    public int compareTo(Key k) {
        if (k == null) return -1;
        if (this.key1 == k.key1) {
            return Arrays.toString(this.key2).compareTo(Arrays.toString(k.key2));
        }
        return Integer.compare(this.key1, k.key1);
    }
}