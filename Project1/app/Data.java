package app;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import dataOutput.Record;

public class Data {
    public static List<Record> records = new ArrayList<>();;
    public Data() throws IOException {
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("Project1/data/data.tsv"));
            reader.readLine();
            String line = reader.readLine();
            while (line != null) {
                String[] words = line.split("\\t");
                Record record = new Record(words[0] , Integer.parseInt(words[2]), Float.parseFloat(words[1]));
                records.add(record);
                analyzeValue(record);
                line = reader.readLine();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Record> getRecord(){
        return records;
    }

    public static String getSize(int size){
        double b = size;
        double kb = size/1000;
        double mb = kb/1000;
        double gb = mb/1000;
        double tb = gb/1000;
        if (kb < 1){
            return String.format("%.2f %s", b, "B");
        }
        else if (mb < 1){
            return String.format("%.2f %s", kb, "KB");
        }
        else if (gb < 1){
            return String.format("%.2f %s", mb, "MB");
        }
        else if (tb < 1){
            return String.format("%.2f %s", gb, "GB");
        }
        else {
            return String.format("%.2f %s", tb, "TB");
        }
    }

    public static int minTconstLen = Integer.MIN_VALUE;
    public static float minAverageRating = Float.MIN_VALUE;
    public static int minNumVotes = Integer.MIN_VALUE;
    public static int maxTconstLen = Integer.MAX_VALUE;
    public static float maxAverageRating = Float.MAX_VALUE;
    public static int maxNumVotes = Integer.MAX_VALUE;

    public static void analyzeValue (Record record) {
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