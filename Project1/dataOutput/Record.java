package dataOutput;

public class Record {
    private String tconst; // 10 bytes: 10 char string
    private int numVotes; // 4 bytes: Float ranging from 1-10
    private float averageRating; // 4 bytes: integer 

    public Record(String tconst,int numVotes,float averageRating){
        this.averageRating=averageRating; // Float ranging from 1-10
        this.tconst=tconst; // 10 char string
        this.numVotes=numVotes; // integer 
    }

    /*
     * Get size of record
     */
    public static int getStorage(){
        return 10+4+4;
    }

    /*
     * Get the tconst of record
     */
    public String getTconst(){
        return tconst;
    }

    /*
     * Get the number of votes for record
     */
    public int getNumVotes(){
        return numVotes;
    }

    /*
     * Get the average rating for record
     */
    public float getAverageRating(){
        return averageRating;
    }

    public void setAverageRating(float new_averageRating){
        this.averageRating=new_averageRating;
    }

    public void setNumVotes(int new_numVotes){
        this.numVotes=new_numVotes;
    }

    public void setTconst(String new_tconst){
        this.tconst=new_tconst;
    }

    /**
     * print the record
     * 
     * @return
     */
    public void printEntry(){
        System.out.printf("tconst: %s, numVotes: %d, averageRating: %f%n",this.tconst,this.numVotes,this.averageRating);
    }
}
