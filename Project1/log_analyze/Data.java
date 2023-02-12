package log_analyze;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Data {
    public static List<Record> records = new ArrayList<>();;
    //private static final String TAG = "Data";
    //public static List<Record> readData() throws IOException {
    public Data() throws IOException {
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("data.tsv"));
            reader.readLine();
			String line = reader.readLine();
            while (line != null) {
                String[] words = line.split("\\t");
                Record record = new Record(words[0], words[1], words[2]);
                records.add(record);
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
}