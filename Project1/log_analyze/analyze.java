package log_analyze;

public class analyze {
    public static int minTconstLen = Integer.MIN_VALUE;
    public static float minAverageRating = Float.MIN_VALUE;
    public static int minNumVotes = Integer.MIN_VALUE;
    public static int maxTconstLen = Integer.MAX_VALUE;
    public static float maxAverageRating = Float.MAX_VALUE;
    public static int maxNumVotes = Integer.MAX_VALUE;
    public static void analyzeValue (Record record) {
        if (record.gettconst().length < minTconstLen){
            minTconstLen = record.gettconst().length;
        }
        if (record.getaverageRating() < minAverageRating){
            minAverageRating = record.getaverageRating();
        }
        if (record.getnumVotes() < minNumVotes){
            minNumVotes = record.getnumVotes();
        }
        if (record.gettconst().length > maxTconstLen){
            maxTconstLen = record.gettconst().length;
        }
        if (record.getaverageRating() > maxAverageRating){
            maxAverageRating = record.getaverageRating();
        }
        if (record.getnumVotes() > maxNumVotes){
            maxNumVotes = record.getnumVotes();
        }
    }

    public static void resetValue(){
        minTconstLen = Integer.MIN_VALUE;
        minAverageRating = Float.MIN_VALUE;
        minNumVotes = Integer.MIN_VALUE;
        maxTconstLen = Integer.MAX_VALUE;
        maxAverageRating = Float.MAX_VALUE;
        maxNumVotes = Integer.MAX_VALUE;
    }
}
