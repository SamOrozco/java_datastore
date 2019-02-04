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
            System.out.println("failed test");
            System.exit(1);
        }


        // advanced query test
        String[] queryArgs1 = new String[]{"-f", "TITLE=shrek or TITLE=1211"};
        List<Row> rows1 = Query.run(queryArgs1);
        if (rows1.size() != 115) {
            System.out.println("failed test");
            System.exit(1);
        }

        // advanced query test
        String[] queryArgs2 = new String[]{"-f", "STB=stb1 or (TITLE=shrek or TITLE=the hobbit)"};
        List<Row> rows2 = Query.run(queryArgs2);
        if (rows2.size() != 230) {
            System.out.println("failed test");
            System.exit(1);
        }

        String[] queryArgs4 =
            new String[]{"-f", "STB=stb1 or (TITLE=shrek and REV=12.1)"};
        List<Row> rows4 = Query.run(queryArgs4, true);
        if (rows4.size() != 13) {
            System.out.println("failed test");
            System.exit(1);
        }


        String[] queryArgs5 =
            new String[]{"-f", "STB=stb1 or (TITLE=shrek and (REV=12.1 or REV=13.1))"};
        List<Row> rows5 = Query.run(queryArgs5, true);
        if (rows5.size() != 13) {
            System.out.println("failed test");
            System.exit(1);
        }


        String[] queryArgs6 = new String[]{"-f", "(STB=stb1 or STB=stb2)"};
        List<Row> rows6 = Query.run(queryArgs6, true);
        if (rows6.size() != 2) {
            System.out.println("failed test");
            System.exit(1);
        }


        String[] queryArgs7 = new String[]{"-f", "(STB=stb1 and STB=stb2)"};
        List<Row> rows7 = Query.run(queryArgs7, true);
        if (rows7.size() != 0) {
            System.out.println("failed test");
            System.exit(1);
        }

        System.out.println("Pass");
    }
}
