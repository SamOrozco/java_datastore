package query;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Filter {
    private static String delim = "=";
    private Map<String, String> filterMap = new HashMap<>();


    public Filter() {
    }

    public Filter(String filter) {
        parseFilter(filter);
    }


    public void parseFilter(String filter) {
        if (filter == null || filter.isEmpty()) {
            return;
        }
        String[] segments = filter.split(Pattern.quote(delim));
        String colName = segments[0].trim();
        if (!validColumnName(colName)) {
            return;
        }
        String value = segments[1].trim();
        addFilter(colName, value);
    }

    private void addFilter(String colName, String value) {
        this.getFilterMap().put(colName, value);
    }


    private boolean validColumnName(String colName) {
        if (colName == null || colName.isEmpty()) {
            return false;
        }
        return true;
    }

    public Map<String, String> getFilterMap() {
        return filterMap;
    }

    public void setFilterMap(Map<String, String> filterMap) {
        this.filterMap = filterMap;
    }
}
