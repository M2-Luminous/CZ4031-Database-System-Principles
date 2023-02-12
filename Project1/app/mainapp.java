package app;

import java.util.*;

import dataOutput.Record;
import dataOutput.disk;
import dataOutput.address;
import nodes.bPlusTree;
import app.utility.log;
import app.utility.utility;

public class mainapp implements constants {
    private static final String TAG = "App";
    public disk disk;
    public bPlusTree index;

    public static void main(String[] args) throws Exception {

        boolean running = true;
        mainapp mainapp = new mainapp();

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
                    System.out.println("----------------Commencing Experiment 1----------------");
                    mainapp.config(BLOCK_SIZE_200);
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

    public void config(int blockSize) throws Exception { // setup disk
        List<Record> records = utility.readRecord(DATA_FILE_PATH);

        disk = new disk(constants.DISK_SIZE, blockSize);
        index = new bPlusTree(blockSize);

        log.i(TAG, "Running program with block size of " + blockSize);
        log.i(TAG, "Prepare to insert records into storage and create index");
        address recordAddr;
        for (Record r : records) {
            // inserting records into disk and create index!
            recordAddr = disk.appendRecord(r);
            index.insert(r.getNumVotes(), recordAddr);
        }
        log.i(TAG, "Record inserted into storage and index created");
        disk.log();
        index.logStructure(1); // printing root and first level?

        index.treeStats();
    }

}
