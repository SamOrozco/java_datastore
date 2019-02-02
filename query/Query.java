package query;

import parser.FileDataStore;

import java.util.*;

public class Query {
    private static Map<String, String> flagMap = new HashMap<>();
    private static final String selectString = "-s";
    private static final String orderString = "-o";
    private static final String filterString = "-f";
    private static Set<String> flags = new HashSet<String>() {{
        add(selectString);
        add(orderString);
        add(filterString);
    }};
    private final String[] args;
    private final FileDataStore store;
    private boolean hasFilter;

    private Query(String[] args) {
        this.args = args;
        this.store = new FileDataStore();
    }


    public static List<Row> run(String[] args) {
        Query query = new Query(args);
        return query.execute();
    }

    public List<Row> execute() {
        if (args.length % 2 != 0) { // if args aren't even it is a bad combo
            throw new RuntimeException("invalid argument combination");
        }
        for (int i = 0; i < args.length; i++) {
            String current = args[i];
            // if we find a flag we like put it and its value in the map
            switch (current) {
                case selectString:
                    flagMap.put(current, args[i + 1]);
                    break;
                case orderString:
                    flagMap.put(current, args[i + 1]);
                    break;
                case filterString:
                    hasFilter = true;
                    flagMap.put(current, args[i + 1]);
                    break;
            }
        }


        Select selector = new Select(flagMap.get(selectString));
        Select ordr = new Select(flagMap.get(orderString));
        List<Row> finalRows = null;
        if (!hasFilter) {
            final List<Row> rows = new LinkedList<>();
            // we need to read all rows
            store.readRowKeys().forEach(key -> {
                List<String> values = store.readRow(key);
                rows.add(new Row(values, selector));
            });
            order(rows, ordr);
            finalRows = rows;
        } else {
            // if we get here a filter was passed
            // build a filter map
            Filter filt = new Filter(flagMap.get(filterString));
            List<String> result = new ArrayList<>();
            for (Map.Entry<String, String> entry : filt.getFilterMap().entrySet()) {
                result.addAll(store.getRowsFromColAndValue(entry.getKey(), entry.getValue()));
            }

            List<Row> rows = store.readRowsFromKeys(selector, result);
            order(rows, ordr);
            finalRows = rows;
        }
        return finalRows;
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
