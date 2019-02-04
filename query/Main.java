package query;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Row> rows = Query.run(args);
        for (Row row : rows) {
            System.out.println(row);
        }
    }

}
