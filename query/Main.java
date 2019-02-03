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

//        StringBuilder builder = new StringBuilder();
//        builder.append("(");
//        int cnt = 0;
//        for (Row row : rows) {
//            builder.append(String.format("STB=%s", row.getStb()));
//            if (cnt++ != rows.size() - 1) {
//                builder.append(" or ");
//            }
//        }
//        builder.append(")");
//        System.out.println(builder.toString());
    }

}
