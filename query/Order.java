package query;

import parser.FileDataStore;

import java.util.Comparator;
import java.util.List;

public class Order {
    private Comparator<Row> rowComparator;
    private String delim = ",";


    /**
     * The order constructor completely initializes the comparator needed for ordering
     * Pass your order string (STB, DATE) to the constructor
     *
     * @param value
     */
    public Order(String value) {
        if (value == null || value.isEmpty()) {
            return;
        }
        String[] segments = value.split(delim);
        for (String seg : segments) {
            Comparator<Row> temp = null;
            if ((temp = getComparator(seg)) != null) {
                appendComparator(temp);
            }
        }
    }


    public void order(List<Row> rows) {
        if (this.rowComparator == null || rows == null) {
            return;
        }
        rows.sort(this.rowComparator);
    }


    private void appendComparator(Comparator<Row> comp) {
        if (rowComparator == null) {
            rowComparator = comp;
            return;
        }
        rowComparator = rowComparator.thenComparing(comp);
    }


    private Comparator<Row> getComparator(String key) {
        switch (key.toUpperCase()) {
            case FileDataStore.STB:
                return Row.stbComp();
            case FileDataStore.TITLE:
                return Row.titleComp();
            case FileDataStore.PROVIDER:
                return Row.providerComp();
            case FileDataStore.DATE:
                return Row.getDateComp();
            case FileDataStore.REV:
                return Row.getRevComp();
            case FileDataStore.VIEW_TIME:
                return Row.getViewTimComp();
            default:
                return null;
        }
    }
}
