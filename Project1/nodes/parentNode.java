package nodes;

//import app.util.Log;
import java.util.ArrayList;

public class parentNode extends Node{
    //private static final String TAG = "Node.P";
    private ArrayList<Node> children;

    public parentNode() {
        super();
        children = new ArrayList<Node>();
    }

    //get all children
    public ArrayList<Node> getChildren() {
        return children;
    }

    //get one child
    public Node getChild(int index) {
        return children.get(index);
    }

    //add child
    public int addChild(Node child) {
        if(children.size() == 0) {
            children.add(child);
            child.setParent(this);
            return 0;
        }

        int key = child.findSmallestKey();
        int smallestKey = this.findSmallestKey();
        int index;

        if(key < smallestKey) {
            this.addKey(smallestKey);
            this.children.add(0, child);
            index = 0;
        }
        else {
            index = this.addKey(key);
            this.children.add(index + 1, child);
        }
        child.setParent(this);
        return index;
    }

    //add child
    public void addChild(Node child, int index) {
        children.add(0, child);
        child.setParent(this);
        deleteAllKey();

        for(int i = 0; i < children.size(); i++) {
            if(i != 0) {
                addKey(children.get(i).findSmallestKey());
            }
        }
    }

    //splitting parent node
    public void splitPrep() {
        deleteAllKey();
        children = new ArrayList<Node>();
    }

    //delete one child
    public void deleteOneChild(Node child) {
        children.remove(child);
        deleteAllKey();

        for(int i = 0; i < children.size(); i++) {
            if(i != 0) {
                addKey(children.get(i).findSmallestKey());
            }
        }
    }

    //delete all children
    public void deleteAllChildren() {
        children = new ArrayList<Node>();
    }

    //get former child
    public Node getFormer(Node node) {
        if(children.indexOf(node) != 0){
            return children.get(children.indexOf(node) - 1);//former index
        }
        return null;
    }

    //get Latter node
    public Node getLatter(Node node) {
        if(children.indexOf(node) != children.size() - 1){
            return children.get(children.indexOf(node) + 1);//latter index
        }
        return null;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < getAllKey().size(); i ++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(String.format("%d:{%d}", i, getOneKey(i) ));
        }
        sb.append("]");
        return sb.toString();
    }

}
