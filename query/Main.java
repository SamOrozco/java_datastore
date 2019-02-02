package query;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        List<Row> rows = Query.run(args);
        String val = String.format("query runtime : %d", System.currentTimeMillis() - startTime);
        System.out.println(val);
        for (Row row : rows) {
            System.out.println(row);
        }
    }

}
