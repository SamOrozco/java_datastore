package query;

import parser.FileDataStore;

import java.util.*;

public class Main {
    private static Map<String, String> flagMap = new HashMap<>();
    private static final String selectString = "-s";
    private static final String orderString = "-o";
    private static final String filterString = "-f";
    private static Set<String> flags = new HashSet<String>() {{
        add(selectString);
        add(orderString);
        add(filterString);
    }};

    public static void main(String[] args) {
        boolean order = false;
        boolean select = false;
        boolean filter = false;
        FileDataStore fileDataStore = new FileDataStore();
        if (args.length % 2 != 0) { // if args aren't even it is a bad combo
            throw new RuntimeException("invalid argument combination");
        }
        for (int i = 0; i < args.length; i++) {
            String current = args[i];
            // if we find a flag we like put it and its value in the map
            switch (current) {
                case selectString:
                    select = true;
                    flagMap.put(current, args[i + 1]);
                    break;
                case orderString:
                    order = true;
                    flagMap.put(current, args[i + 1]);
                    break;
                case filterString:
                    filter = true;
                    flagMap.put(current, args[i + 1]);
                    break;
            }
        }


        Select selector = new Select(flagMap.get(selectString));
        Select ordr = new Select(flagMap.get(orderString));
        List<Row> finalRows = null;
        if (!filter) {
            final List<Row> rows = new LinkedList<>();
            // we need to read all rows
            fileDataStore.readRowKeys().forEach(key -> {
                List<String> values = fileDataStore.readRow(key);
                rows.add(new Row(values, selector));
            });
            order(rows, ordr);
            finalRows = rows;
        } else {

        }

        for (Row row : finalRows) {
            System.out.println(row);
        }
    }


    private static void order(List<Row> rows, Select order) {
        Comparator<Row> rowComparator = null;
        if (order.isStb()) {
            rowComparator = addComp(rowComparator, Row.stbComp());
        }
        if (order.isTitle()) {
            rowComparator = addComp(rowComparator, Row.titleComp());
        }

        if (order.isProvider()) {
            rowComparator = addComp(rowComparator, Row.providerComp());
        }

        if (order.isDate()) {
            rowComparator = addComp(rowComparator, Row.getDateComp());
        }

        if (order.isRev()) {
            rowComparator = addComp(rowComparator, Row.getRevComp());
        }

        if (order.isViewTime()) {
            rowComparator = addComp(rowComparator, Row.getViewTimComp());
        }
        if (rowComparator != null) {
            rows.sort(rowComparator);
        }
    }

    private static Comparator<Row> addComp(Comparator<Row> rowComparator, Comparator<Row> comp) {
        if (rowComparator == null) {
            rowComparator = comp;
            return comp;
        } else {
            return rowComparator.thenComparing(comp);
        }
    }

}
