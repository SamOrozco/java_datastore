package parser;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        Parser parser = Parser.newPipeParser();
        parser.store("/Users/samorozco/Projects/java_datastore/java_parser/files/test6000.txt");
        System.out.println(String.format("runtime ms : %d", System.currentTimeMillis() - start));
    }
}
