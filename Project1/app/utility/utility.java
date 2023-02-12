package app.utility;
import app.constants;
import dataOutput.Record;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class utility {
    private static final String TAG = "Utility";

    public static List<Record> readRecord(String path) throws Exception {
		File dataFile = new File(path);
		log.i(TAG, "Loading records from "+path);
		if (!dataFile.exists()) {
			// file is not exist, try to add current directory and try again

			dataFile = new File(constants.PROJECT_DIRECTORY, path);
			log.i(TAG, "Failed, re-attempt to load record from "+dataFile.getAbsolutePath());
			if (!dataFile.exists()){
				throw new FileNotFoundException("File not exist");
			}
		}

		BufferedReader br = null;
		List<Record> records = new ArrayList<>();
		String line;
		String[] parts = null;
		try {
			br = new BufferedReader( new FileReader(dataFile));
			// reading header first (to be skipped)
			br.readLine();
			while((line = br.readLine()) != null) {
				parts = line.split("\\t");
				Record record = new Record(parts[0], Float.parseFloat(parts[1]), Integer.parseInt( parts[2]));
				records.add( record );
				analyzer.analysis(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				}catch (IOException e) {
					log.e(e.getMessage());
				}
			}
		}
		log.d(TAG, "total records: "+records.size());
		analyzer.log();
		return records;
	}

	/**
	 * Convert file size into human-readable format
	 * @param size file size in bytes
	 * @return formatted size
	 */
	public static String formatFileSize(int size){
		String[] suffix = { "B", "KB", "MB", "GB", "TB" };
		int order = 0;
		if (size>0){
			order = (int) (Math.log(size)/Math.log(1000));
		}
		double normSize = size / Math.pow(1000, order);
		return String.format("%.2f %s", normSize, suffix[order]);
	}


	public static List<Record> generateRecords(int num){
		ArrayList<Record> records = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			String tconst = String.format("tt%08d", i+1);
			records.add( new Record(tconst, 0f, i+1));
		}
		return records;
	}

	public static List<Record> generateRecords(int num, int duplicates){
		ArrayList<Record> records = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			String tconst = String.format("tt%08d", i+1);
			records.add( new Record(tconst, 0f, i/duplicates));
		}
		return records;
	}
}
