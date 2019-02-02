package parser;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        parser.Parser parse = parser.Parser.newPipeParser();
        String location = args[0];
        parse.store(location);
        System.out.println(String.format("runtime ms : %d", System.currentTimeMillis() - start));
    }
}
