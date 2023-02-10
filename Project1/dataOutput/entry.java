package Project1.dataOutput;

public class entry {
    private String tconst;
    private int numVotes;
    private float averageRating;

    public entry(String tconst,int numVotes,float averageRating){
        this.averageRating=averageRating;
        this.tconst=tconst;
        this.numVotes=numVotes;
    }

    public static int get_storage(){
        return 10+4+4;
    }
    public String gettconst(){
        return tconst;
    }
    public int getnumVotes(){
        return numVotes;
    }
    public float getaverageRating(){
        return averageRating;
    }
    public void setaverageRating(float new_averageRating){
        this.averageRating=new_averageRating;
    }
    public void setnumVotes(int new_numVotes){
        this.numVotes=new_numVotes;
    }
    public void setaverageRating(String new_tconst){
        this.tconst=new_tconst;
    }
    public void printentry(){
        System.out.printf("tconst: %s, numVotes: %d, averageRating: %f%n",this.tconst,this.numVotes,this.averageRating);
    }
}
