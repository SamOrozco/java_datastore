package test;

import parser.FileDataStore;
import parser.Parser;
import parser.RowStore;
import query.Query;
import query.Row;

import java.io.FileNotFoundException;
import java.util.List;

public class QueryParserTest {
    public static final String fileLocation = "files/test_lock.txt";

    public static void main(String[] args) throws FileNotFoundException {
        Parser parser = Parser.newPipeParser();
        parser.store(fileLocation);
        RowStore store = new FileDataStore();
        if (!store.isInitialized()) {
            System.out.println("file data store init isn't working");
            System.exit(1);
        }


        // simple query test
        String[] queryArgs = new String[]{"-s", "STB,TITLE", "-f", "TITLE=shrek", "-o", "STB"};
        List<Row> rows = Query.run(queryArgs);
        if (rows.size() != 115) {
            System.out.println("first query test count not correct");
            System.exit(1);
        }


        // advanced query test
        String[] queryArgs1 = new String[]{"-f", "TITLE=shrek or TITLE=1211"};
        List<Row> rows1 = Query.run(queryArgs1);
        if (rows1.size() != 115) {
            System.out.println("first query test count not correct");
            System.exit(1);
        }

        // advanced query test
        String[] queryArgs2 = new String[]{"-f", "STB=stb1 or (TITLE=shrek or TITLE=the hobbit)"};
        List<Row> rows2 = Query.run(queryArgs2);
        if (rows2.size() != 230) {
            System.out.println("first query test count not correct");
            System.exit(1);
        }

        // advanced query test
        String[] queryArgs3 = new String[]{"-f", "STB=stb1 or (TITLE=the hobbit"};
        try { // we expect this to throw an exception
            List<Row> rows3 = Query.run(queryArgs3);
            System.out.println("failed expected exception");
            System.exit(1);
        } catch (Exception e) {
        }

        String[] queryArgs4 =
            new String[]{"-f", "STB=stb1 or (TITLE=shrek and REV=12.1)"};
        List<Row> rows4 = Query.run(queryArgs4, true);
        if (rows4.size() != 13) {
            System.out.println("first query test count not correct");
            System.exit(1);
        }
        System.out.println("Pass");
    }
}
