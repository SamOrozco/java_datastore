package query;

import parser.FileDataStore;
import parser.RowStore;
import query.filter.Filter;

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
    private final RowStore store;
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
        Order order = new Order(flagMap.get(orderString));
        List<Row> finalRows = null;

        // if there is no filter fetch all rows
        if (!hasFilter) {
            final List<Row> rows = new LinkedList<>();
            store.readRowKeys().forEach(key -> {
                List<String> values = store.readRow(key);
                rows.add(new Row(values, selector));
            });
            finalRows = rows;
        } else {
            // if we get here a filter was passed
            // build a filter map
            Filter filter = new Filter(flagMap.get(filterString), store, selector);
            Collection<String> unqKeys = filter.eval();
            // we have all the keys we want to query before we read anything from disk
            finalRows = store.readRowsFromKeys(selector, unqKeys);
        }

        System.out.println(String.format("row count %d", finalRows.size()));
        order.order(finalRows);
        return finalRows;
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
