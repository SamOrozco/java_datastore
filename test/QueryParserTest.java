package test;

import parser.FileDataStore;
import parser.Parser;
import query.Query;
import query.Row;

import java.io.FileNotFoundException;
import java.util.List;

public class QueryParserTest {
    public static final String fileLocation = "files/test1.txt";

    public static void main(String[] args) throws FileNotFoundException {
        Parser parser = Parser.newPipeParser();
        parser.store(fileLocation);
        FileDataStore store = new FileDataStore();
        if (!store.isInitialized()) {
            System.out.println("file data store init isn't working");
            System.exit(1);
        }

        String[] queryArgs = new String[]{"-s", "STB,TITLE", "-f", "TITLE=sam orozco", "-o", "STB"};
        List<Row> rows = Query.run(queryArgs);
        if (rows.size() > 2) {
            System.out.println("first query test count not correct");
            System.exit(1);
        }
        rows.forEach(System.out::println);
    }
}