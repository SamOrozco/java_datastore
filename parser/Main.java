package parser;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        parser.Parser parse = parser.Parser.newPipeParser();
        parse.store("/Users/samorozco/Projects/java_datastore/files/test6000.txt");
        System.out.println(String.format("runtime ms : %d", System.currentTimeMillis() - start));
    }
}
