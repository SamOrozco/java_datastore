package parser;

import query.Row;
import query.Select;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileDataStore implements RowStore {
    public static final String pathDelim = isWindows() ? "\\" : "/";
    public static final String newLine = "\r\n";
    private String rootDir = ".";
    private String rowDir = rootDir + pathDelim + ".row";
    private String rowKeyLoc = rowDir + pathDelim + ".keys";
    private String rootColDir = rootDir + pathDelim + ".col";

    public static final String STB = "STB";
    public static final String TITLE = "TITLE";
    public static final String PROVIDER = "PROVIDER";
    public static final String DATE = "DATE";
    public static final String REV = "REV";
    public static final String VIEW_TIME = "VIEW_TIME";

    public FileDataStore() {
        init();
    }

    public FileDataStore(String rootDir, String rowDir, String rootColDir) {
        this.rootDir = rootDir;
        this.rowDir = rowDir;
        this.rootColDir = rootColDir;
        init();
    }


    /**
     * This method checks to see if the required file structure exists. If it doesn't this method
     * will try to create it and throw any exception that occurs
     */
    public void init() {
        if (!createDirIfNotExists(rootDir)) {
            throw new RuntimeException(String.format("unable to init dir %s", rootDir));
        }
        if (!createDirIfNotExists(rowDir)) {
            throw new RuntimeException(String.format("unable to init dir %s", rowDir));
        }
        if (!createDirIfNotExists(rootColDir)) {
            throw new RuntimeException(String.format("unable to init dir %s", rootColDir));
        }
    }


    public boolean isInitialized() {
        return dirExists(rootDir) && dirExists(rowDir) && dirExists(rootColDir);
    }

    public List<String> getRowsFromColAndValue(String colName, String value) {
        int val = -1;
        switch (colName.toUpperCase()) {
            case STB:
                val = Converters.getStringConverter().convert(value);
                return getRowsIds(getColDataDir(colName, String.valueOf(val)));
            case TITLE:
                val = Converters.getStringConverter().convert(value);
                return getRowsIds(getColDataDir(colName, String.valueOf(val)));
            case PROVIDER:
                val = Converters.getStringConverter().convert(value);
                return getRowsIds(getColDataDir(colName, String.valueOf(val)));
            case DATE:
                val = Converters.getDateConverter().convert(value);
                return getRowsIds(getColDataDir(colName, String.valueOf(val)));
            case REV:
                val = Converters.getFloatConverter().convert(value);
                return getRowsIds(getColDataDir(colName, String.valueOf(val)));
            case VIEW_TIME:
                val = Converters.getTimeConverter().convert(value);
                return getRowsIds(getColDataDir(colName, String.valueOf(val)));
            default:
                return new ArrayList<>();
        }
    }


    /**
     * This method any error that occurs just means that data doesn't exists
     * so it will return an empty record set
     *
     * @param loc
     * @return
     */
    private List<String> getRowsIds(String loc) {
        try {
            return getLineStream(loc).collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }


    public void writeRow(int rowKey, String[] values) {
        String contents = getRowVal(values);
        String rowLocation = getRowLocation(rowKey);
        storeContents(rowLocation, contents);
    }


    public List<String> readRow(String key) {
        String rowLoc = getRowLocation(key);
        List<String> list = new ArrayList<>(Parser.LINE_LENGTH);
        getLineStream(rowLoc).forEach(list::add);
        return list;
    }


    public List<Row> readRowsFromKeys(Select select, Collection<String> values) {
        List<Row> result = new ArrayList<>();
        for (String key : values) {
            List<String> vals = readRow(key);
            result.add(new Row(vals, select));
        }
        return result;
    }


    public void writeColumn(String columnName, Column column) {
        String path = getColDir(columnName);
        createDirIfNotExists(path);
        for (Map.Entry<Integer, Set<Integer>> vals : column.getData().entrySet()) {
            String location = path + pathDelim + vals.getKey();
            String contents = getRowVal(vals.getValue());
            appendFile(location, contents);
        }
    }

    public void storeRowKeys(String contents) {
        storeContents(rowKeyLoc, contents);
    }

    public Stream<String> readRowKeys() {
        return getLineStream(rowKeyLoc);
    }

    @Override
    public List<String> getRowKeys() {
        return readRowKeys().collect(Collectors.toList());
    }

    private String getColDir(String colName) {
        return rootColDir + pathDelim + colName;
    }

    private String getColDataDir(String colName, String value) {
        return rootColDir + pathDelim + colName + pathDelim + value;
    }

    public void storeContents(String location, String contents) {
        File file = new File(location);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(contents);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void appendFile(String location, String contents) {
        File file = new File(location);
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.append(contents);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRowVal(Collection<Integer> vals) {
        StringBuilder bldr = new StringBuilder();
        for (Integer val : vals) {
            bldr.append(val).append(newLine);
        }
        return bldr.toString();
    }

    private String getRowVal(String[] values) {
        StringBuilder bldr = new StringBuilder();
        for (int index : Parser.INDEXES) {
            bldr.append(values[index]).append(newLine);
        }
        return bldr.toString();
    }


    private String getRowLocation(int key) {
        return rowDir + pathDelim + key;
    }

    private String getRowLocation(String key) {
        return rowDir + pathDelim + key;
    }


    private boolean createDirIfNotExists(String location) {
        File file = new File(location);
        if (!file.exists()) {
            return file.mkdir();
        }
        return file.isDirectory();
    }


    private boolean deleteDirIfNotExists(String location) {
        File file = new File(location);
        if (!file.exists()) {
            return false;
        }
        try {
            Files.delete(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private boolean dirExists(String location) {
        File file = new File(location);
        return file.exists() && file.isDirectory();
    }


    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().contains("windows");
    }


    /**
     * This method assumes any errors that occur are due to data not exiting and will
     * return an empty record set
     *
     * @param fileLocation
     * @return
     */
    public static Stream<String> getLineStream(String fileLocation) {
        try {
            return Files.lines(Paths.get(fileLocation));
        } catch (IOException e) {
            return Stream.empty();
        }
    }

}
