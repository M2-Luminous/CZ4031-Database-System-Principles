package app;

import java.text.DecimalFormat;
import java.util.*;

import dataOutput.Record;
import dataOutput.disk;
import dataOutput.address;
import nodes.bPlusTree;
import log_analyze.Data;
//import log_analyze;

public class mainapp implements constants {
    private static final String TAG = "App";
    public disk disk;
    public bPlusTree index;

    public static void main(String[] args) throws Exception {

        boolean running = true;
        mainapp mainapp = new mainapp();
        boolean exp1 = false;

        while (true) {
            System.out.println("CZ4031 Database System Principles Project 1");
            System.out.println("1) Experiment 1 ");
            System.out.println("2) Experiment 2 ");
            System.out.println("3) Experiment 3 ");
            System.out.println("4) Experiment 4 ");
            System.out.println("5) Experiment 5 ");
            System.out.println("Please enter 1 - 5 for the different experiments");
            System.out.println("Please enter -1 to exit program");
            Scanner choice = new Scanner(System.in);
            String userInput = choice.nextLine();
            System.out.println("You have selected " + userInput);
            int userInt = Integer.parseInt(userInput);
            if (userInt == -1) {
                System.out.println("-------------Exiting Program-----------------");
                System.exit(0);
            }
            switch (userInt) {
                case 1:
                    if (exp1 == true) {
                        break;
                    }
                    System.out.println("----------------Commencing Experiment 1----------------");
                    exp1 = mainapp.config(BLOCK_SIZE_200);
                    System.out.println();
                    System.out.println("-------------------Experiment 1 has ended-------------------");
                    break;
                case 2:
                    System.out.println("----------------Commencing Experiment 2----------------");
                    System.out.println("-------------------Experiment 2 has ended-------------------");
                    break;
                case 3:
                    System.out.println("----------------Commencing Experiment 3----------------");
                    System.out.println("-------------------Experiment 3 has ended-------------------");
                    break;
                case 4:
                    System.out.println("----------------Commencing Experiment 4----------------");
                    System.out.println("-------------------Experiment 4 has ended-------------------");
                    break;
                case 5:
                    System.out.println("----------------Commencing Experiment 5----------------");
                    System.out.println("-------------------Experiment 5 has ended-------------------");
                    break;
                default:
                    System.out.println("You have entered a invalid input");
                    break;
            }

        }
    }

    public boolean config(int blockSize) throws Exception { // setup disk
        // List<Record> records = Data.readRecord(DATA_FILE_PATH); // read file

        Data data = new Data();
        List<Record> records = data.getRecord(); // read file

        disk = new disk(constants.DISK_SIZE, blockSize);
        index = new bPlusTree(blockSize);

        System.out.println("Block size of " + format(blockSize));
        System.out.println("Inserting records into Storage");
        // log.i(TAG, "Running program with block size of " + blockSize);
        // log.i(TAG, "Prepare to insert records into storage and create index");
        address recordAddr;
        for (Record r : records) {
            // inserting records into disk and create index!
            int lastBlkId = disk.getLastBlockId();
            System.out.println(lastBlkId);
            recordAddr = disk.insertRecord(r, lastBlkId);
            index.insert(r.getNumVotes(), recordAddr);
        }
        System.out.println("Records insertion completed");

        System.out.println("Disk size is : " + format(disk.getUsedSize()) + " / " + format(disk.getDiskSize()));
        System.out.println("Number of records is : " + disk.getRecordCounts());
        System.out.println("Size of a record is : " + disk.getUsedSize() / disk.getRecordCounts()); // used size of
        // storage / total
        // number of records
        System.out.println("Number of blocks for storing the data : " + disk.getBlocksCount());
        // disk.getBlock().get(0).printBlock();
        // int test = disk.getBlock().get(0).getMaxNumOfRecords();
        // System.out.println("max no of rec in a block is : " + test);

        index.logStructure(1); // printing root and first level?

        index.treeStats();

        return true;
    }

    public String format(int size) {
        final DecimalFormat df = new DecimalFormat("0.00");
        String[] memType = { "B", "KB", "MB", "GB", "TB" };
        int multiplier = 0;
        if (size > 0) {
            multiplier = (int) (Math.log(size) / Math.log(1000));
        }
        double actSize = size / Math.pow(1000, multiplier);
        return df.format(actSize) + memType[multiplier];
    }

    public void experiment3() { // numvotes 500
        ArrayList<address> RecordAddresses = index.getRecordsWithKey(500);
        ArrayList<Record> records = disk.getRecords(RecordAddresses);
        // records collected, do calculate average rating
        double avgRating = 0;
        for (Record record : records) {
            avgRating += record.getAverageRating();
        }
        avgRating /= records.size();
        System.out.println("Average rating for records with numvotes  equals to 500 : is " + avgRating);
    }

    public void experiment4() {
        ArrayList<address> e4RecordAddresses = index.getRecordsWithKeyInRange(30000, 40000);
        ArrayList<Record> records = disk.getRecords(e4RecordAddresses);
        // records collected, do calculate average rating
        double avgRating = 0;
        for (Record record : records) {
            avgRating += record.getAverageRating();
        }
        avgRating /= records.size();
        System.out.println("Average rating for records with numvotes between 30k - 40k : is " + avgRating);
    }

    public void experiment5() { // delete numvote == 1k
        index.deleteKey(1000);
    }

}
