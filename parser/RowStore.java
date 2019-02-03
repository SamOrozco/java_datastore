package parser;

import query.Row;
import query.Select;

import java.util.List;
import java.util.stream.Stream;

public interface RowStore {
    /**
     * This method will return all rows associated with the given column and value
     *
     * @param colName
     * @param value
     * @return
     */
    List<String> getRowsFromColAndValue(String colName, String value);

    /**
     * This method persists a row file with the given key and values
     *
     * @param rowKey
     * @param values
     */
    void writeRow(int rowKey, String[] values);

    /**
     * This method reads the row data with the given rowKey
     *
     * @param key
     * @return
     */
    List<String> readRow(String key);

    /**
     * This method selects the given values in the Select operator for each row keys given
     *
     * @param select
     * @param values
     * @return
     */
    List<Row> readRowsFromKeys(Select select, List<String> values);

    /**
     * This method persists the given column for the given columnName
     * This me
     *
     * @param columnName
     * @param column
     */
    void writeColumn(String columnName, Column column);


    /**
     * This method stores the rowKeyContents
     *
     * @param contents
     */
    void storeRowKeys(String contents);

    /**
     * This method should return all existing row keys as stream
     *
     * @return
     */
    Stream<String> readRowKeys();


    /**
     * This method should return all existing row keys
     *
     * @return
     */
    List<String> getRowKeys();

    /**
     * This method should return true if the data store is properly initialized and
     * ready to be written to
     *
     * @return
     */
    boolean isInitialized();

    /**
     * This method is meant to store the contents in the given location
     *
     * @param loc
     * @param contents
     */
    void storeContents(String loc, String contents);
}
