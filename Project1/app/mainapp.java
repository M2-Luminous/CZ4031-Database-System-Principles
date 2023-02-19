package app;

import java.text.DecimalFormat;
import java.util.*;

import dataOutput.Record;
import dataOutput.disk;
import dataOutput.address;
import nodes.bPlusTree;
import log_analyze.Data;
import dataOutput.block;

public class mainapp implements constants {
    private static final String TAG = "App";
    public disk disk;
    public bPlusTree index;

    public static void main(String[] args) throws Exception {

        boolean running = true;
        mainapp mainapp = new mainapp();
        boolean exp1 = false;
        boolean exp2 = false;
        boolean exp3 = false;
        boolean exp4 = false;
        boolean exp5 = false;

        long startTime;
        long endTime;
        long duration;

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
                        System.out.println("Experiment has already concluded.");
                        break;
                    }
                    System.out.println("----------------Commencing Experiment 1----------------");
                    exp1 = mainapp.config(BLOCK_SIZE_200);
                    System.out.println();
                    System.out.println("-------------------Experiment 1 has ended-------------------");
                    break;
                case 2:
                    if (exp2 == true){
                        System.out.println("Experiment has already concluded.");
                        break;
                    }
                    System.out.println("----------------Commencing Experiment 2----------------");
                    exp2 = mainapp.experiment2();
                    System.out.println("-------------------Experiment 2 has ended-------------------");
                    break;
                case 3:
                    if (exp3 == true) {
                        System.out.println("Experiment has already concluded.");
                    break;
                    }
                    System.out.println("----------------Commencing Experiment 3----------------");
                    startTime  = System.nanoTime();
                    exp3 = mainapp.experiment3();
                    endTime = System.nanoTime();
                    duration = (endTime - startTime)/1000;
                    System.out.println("Duration of this experiment is " + duration + " seconds");
                    startTime  = System.nanoTime();
                    mainapp.doLinearScanAve(500);
                    endTime = System.nanoTime();
                    duration = (endTime - startTime)/1000;
                    System.out.println("Duration of this experiment is " + duration + " seconds");
                    System.out.println("-------------------Experiment 3 has ended-------------------");
                    break;
                case 4:
                if (exp4 == true) {
                    System.out.println("Experiment has already concluded.");
                break;
                }
                    System.out.println("----------------Commencing Experiment 4----------------");
                    startTime  = System.nanoTime();
                    exp4 = mainapp.experiment4();
                    endTime = System.nanoTime();
                    duration = (endTime - startTime)/1000;
                    System.out.println("Duration of this experiment is " + duration + " seconds");
                    startTime  = System.nanoTime();
                    mainapp.doLinearScanRange(30000, 40000);
                    endTime = System.nanoTime();
                    duration = (endTime - startTime)/1000;
                    System.out.println("Duration of this experiment is " + duration + " seconds");
                    System.out.println("-------------------Experiment 4 has ended-------------------");
                    break;
                case 5:
                if (exp5 == true) {
                    System.out.println("Experiment has already concluded.");
                break;
                }
                    System.out.println("----------------Commencing Experiment 5----------------");
                    startTime  = System.nanoTime();
                    exp5 = mainapp.experiment5();
                    endTime = System.nanoTime();
                    duration = (endTime - startTime)/1000;
                    System.out.println("Duration of this experiment is " + duration + " seconds");
                    startTime  = System.nanoTime();
                    mainapp.doLinearScanDlt(1000);
                    endTime = System.nanoTime();
                    duration = (endTime - startTime)/1000;
                    System.out.println("Duration of this experiment is " + duration + " seconds");
                    System.out.println("-------------------Experiment 5 has ended-------------------");
                    break;
                default:
                    System.out.println("You have entered a invalid input");
                    break;
            }

        }
    }

    public boolean config(int blockSize) throws Exception { // setup disk

        Data data = new Data();
        List<Record> records = data.getRecord(); // read file

        disk = new disk(constants.DISK_SIZE, blockSize);
        index = new bPlusTree(blockSize);

        System.out.println("Block size of " + format(blockSize));
        System.out.println("Inserting records into Storage");
        address recordAddr;
        for (Record r : records) {
            // inserting records into disk and create index!
            int lastBlkId = disk.getLastBlockId();
            recordAddr = disk.insertRecord(r, lastBlkId);
            index.insert(r.getNumVotes(), recordAddr);
        }
        System.out.println("Records insertion completed");
        System.out.println("Experiment 1 Questions : ");
        System.out.println("Number of records is : " + disk.getRecordCounts());
        System.out.println("Size of a record is : " + disk.getUsedSize() / disk.getRecordCounts()); // used size of
        // storage / total
        // number of records
        System.out.println("Number of records stored in a block is : " + blockSize/(disk.getUsedSize() / disk.getRecordCounts()));
        System.out.println("Number of blocks for storing the data : " + disk.getBlocksCount());
        //System.out.println("Experiment 2 Questions : ");
        
        //index.treeStats();
        
        return true;
    }

    public boolean experiment2(){
        //System.out.println("Experiment 2 Questions : ");
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

    public boolean experiment3() { // numvotes 500
        ArrayList<address> RecordAddresses = index.getRecordsWithKey(500);
        ArrayList<Record> records = disk.getRecords(RecordAddresses);
        // records collected, do calculate average rating
        double avgRating = 0;
        for (Record record : records) {
            avgRating += record.getAverageRating();
        }
        avgRating /= records.size();
        System.out.println("Average rating for records with numvotes equals to 500 is " + avgRating);
        return true;
    }

    public boolean experiment4() { // range numvote = 30k - 40k
        ArrayList<address> e4RecordAddresses = index.getRecordsWithKeyInRange(30000, 40000);
        ArrayList<Record> records = disk.getRecords(e4RecordAddresses);
        // records collected, do calculate average rating
        double avgRating = 0;
        for (Record record : records) {
            avgRating += record.getAverageRating();
        }
        avgRating /= records.size();
        System.out.println("Average rating for records with numvotes between 30k - 40k is " + avgRating);
        return true;
    }

    public boolean experiment5() { // delete numvote == 1k
        index.deleteKey(1000);
        return true;
    }

    public void doLinearScanAve(int numVotes){
        ArrayList<block> blocks = new ArrayList<>();
        blocks = disk.getBlocks();
        int count = 0;
        int hit = 0;
        double ave = 0;
        for(int i = 0 ; i < blocks.size() ; i++){
            count++; //no of blocks accessed
            block block = blocks.get(i);
            int currentNoOfRecords = block.getCurrentNoOfRecords();
            for(int j = 0 ; j<currentNoOfRecords ; j++){
                Record record = block.getRecord(j);
                if(record.getNumVotes() == numVotes ){
                    hit++; //record hit
                    ave+=record.getAverageRating();
                }
            }
        }
        ave = ave/hit;
        System.out.println("-------------Linear scan output-----------");
        System.out.println("No of Blocks accessed is " + count);
        System.out.println("No of Records found with "+ numVotes + " numvotes is "+ hit);
        System.out.println("Average is  " + ave);
    }

    public void doLinearScanDlt(int numVotes){
        ArrayList<block> blocks = new ArrayList<>();
        blocks = disk.getBlocks();
        int count = 0;
        int hit = 0;
        for(int i = 0 ; i < blocks.size() ; i++){
            count++; //no of blocks accessed
            block block = blocks.get(i);
            int currentNoOfRecords = block.getCurrentNoOfRecords();
            for(int j = 0 ; j<currentNoOfRecords ; j++){
                Record record = block.getRecord(j);
                if(record.getNumVotes() == numVotes ){
                    hit++; //record hit
                }
            }
        }
        System.out.println("-------------Linear scan output-----------");
        System.out.println("No of Blocks accessed is " + count);
        System.out.println("No of Records found with "+ numVotes + " numvotes is "+ hit);
    }

    public void doLinearScanRange(int numVotesSmall , int numVotesLarge){
        ArrayList<block> blocks = new ArrayList<>();
        blocks = disk.getBlocks();
        int count = 0;
        int hit = 0;
        double ave = 0;
        for(int i = 0 ; i < blocks.size() ; i++){
            count++; //no of blocks accessed
            block block = blocks.get(i);
            int currentNoOfRecords = block.getCurrentNoOfRecords();
            for(int j = 0 ; j<currentNoOfRecords ; j++){
                Record record = block.getRecord(j);
                if(record.getNumVotes() >=numVotesSmall && record.getNumVotes()<=numVotesLarge ){
                    hit++; //record hit
                    ave+=record.getAverageRating();
                }
            }
        }
        ave = ave/hit;
        System.out.println("-------------Linear scan output-----------");
        System.out.println("No of Blocks accessed is " + count);
        System.out.println("No of Records found with range "+ numVotesSmall + " and " + numVotesLarge + " numvotes is "+ hit);
        System.out.println("Average is  " + ave);
    }

}
