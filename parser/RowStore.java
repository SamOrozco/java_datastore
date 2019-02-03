package parser;

import query.Row;
import query.Select;

import java.util.List;
import java.util.stream.Stream;

public interface RowStore {
    List<String> getRowsFromColAndValue(String colName, String value);

    void writeRow(int rowKey, String[] values);

    List<String> readRow(String key);

    List<Row> readRowsFromKeys(Select select, List<String> values);

    void writeColumn(String columnName, Column column);

    void writeKeyFile(String contents);

    Stream<String> readRowKeys();

    boolean isInitialized();

    void writeFile(String loc, String contents);
}
