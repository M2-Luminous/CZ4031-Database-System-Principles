package log_analyze;

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
                analyze.analyzeValue(record);
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
}