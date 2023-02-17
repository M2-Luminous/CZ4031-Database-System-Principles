package dataOutput;

public class Entry {
    private String tconst;
    private int numVotes;
    private float averageRating;

    public Entry(String tconst, int numVotes, float averageRating) {
        this.averageRating = averageRating;
        this.tconst = tconst;
        this.numVotes = numVotes;
    }


    public static int getStorage() {
        return 10 + 4 + 4;
    }

    public String getTconst() {
        return tconst;
    }

    public int getNumVotes() {
        return numVotes;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float new_averageRating) {
        this.averageRating = new_averageRating;
    }

    public void setNumVotes(int new_numVotes) {
        this.numVotes = new_numVotes;
    }

    public void setAverageRating(String new_tconst) {
        this.tconst = new_tconst;
    }

    public void printEntry() {
        System.out.printf("tconst: %s, numVotes: %d, averageRating: %f%n", this.tconst, this.numVotes,
                this.averageRating);
    }
}
