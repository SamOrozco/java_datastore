package parser;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Column implements Converter {
    private Map<Integer, Set<Integer>> data;

    public Column() {
        setData(new ConcurrentHashMap<>());
    }

    protected void add(Integer value, int rowNum) {
        getData().computeIfPresent(value, (k, v) -> {
            v.add(rowNum);
            return v;
        });
        getData().computeIfAbsent(value, (k) -> {
            Set<Integer> rowSet = new HashSet<>();
            rowSet.add(rowNum);
            return rowSet;
        });
    }

    public void addValue(String value, int rowKey) {
        add(convert(value), rowKey);
    }

    public Map<Integer, Set<Integer>> getData() {
        return data;
    }

    private void setData(Map<Integer, Set<Integer>> data) {
        this.data = data;
    }
}
