package Project1.nodes;

//import app.storage.Address;
//import app.util.Log;
import java.util.ArrayList;




public class bPlusTree {
    private static final int SIZE_POINTER = 6;
    private static final int SIZE_KEY = 4;
    private static final String TAG = "bplustree";

    //int maxKeys;
    //int parentMinKeys;
    //int leafMinKeys;
    //Node root;
    //int height;
    //int nodeCount;
    //int deletedCount;

    public bPlusTree(int blockSize){
        int maxKey = (blockSize - SIZE_POINTER) / (SIZE_KEY + SIZE_POINTER);     //maximum number of keys available
        int parentMinKey = (int) Math.floor(maxKey/2);                           //divides 2 and rounds down 
        int leafMinKey = (int) Math.floor((maxKey+1)/2);
        
        //Log.i(TAG, "initial : blockSize = " + blockSize + ", maxKey = " + maxKey);
        //Log.i(TAG, "minKeys : parent = " + parentMinKey + ", leaf = " + leafMinKey);

        //Node root = createFirst();
        int nodeCount = 0;
        int deleteCount = 0;
    }

    //public LeafNode createFirst(){

    //}
}
