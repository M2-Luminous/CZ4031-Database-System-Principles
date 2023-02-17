package log_analyze;

import dataOutput.Entry;

public class analyze {
    public static int minTconstLen = Integer.MIN_VALUE;
    public static float minAverageRating = Float.MIN_VALUE;
    public static int minNumVotes = Integer.MIN_VALUE;
    public static int maxTconstLen = Integer.MAX_VALUE;
    public static float maxAverageRating = Float.MAX_VALUE;
    public static int maxNumVotes = Integer.MAX_VALUE;
    public static void analyzeValue (Entry record) {
        if (record.getTconst().length() < minTconstLen){
            minTconstLen = record.getTconst().length();
        }
        if (record.getAverageRating() < minAverageRating){
            minAverageRating = record.getAverageRating();
        }
        if (record.getNumVotes() < minNumVotes){
            minNumVotes = record.getNumVotes();
        }
        if (record.getTconst().length() > maxTconstLen){
            maxTconstLen = record.getTconst().length();
        }
        if (record.getAverageRating() > maxAverageRating){
            maxAverageRating = record.getAverageRating();
        }
        if (record.getNumVotes() > maxNumVotes){
            maxNumVotes = record.getNumVotes();
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
