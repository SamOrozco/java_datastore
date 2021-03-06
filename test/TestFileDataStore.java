package test;

import parser.FileDataStore;
import parser.RowStore;

import java.io.FileNotFoundException;

public class TestFileDataStore {
    private static final String rootTestDir = "." + FileDataStore.pathDelim + "files";
    private static final String testLocation = rootTestDir + FileDataStore.pathDelim + "test10.txt";

    public static void main(String[] args) throws FileNotFoundException {
        // init file structure test
        RowStore fileDataStore = new FileDataStore();
        if (!fileDataStore.isInitialized()) {
            System.out.println("file data store init isn't working");
            System.exit(1);
        }
    }
}
