package parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class FileDataStore {
    private static final String pathDelim = isWindows() ? "\\" : "/";
    public static final String newLine = "\r\n";
    private String rootDir = ".";
    private String rowDir = rootDir + pathDelim + ".row";
    private String rowKeyLoc = rowDir + pathDelim + ".keys";
    private String rootColDir = rootDir + pathDelim + ".col";

    public FileDataStore() {
    }

    public FileDataStore(String rootDir, String rowDir, String rootColDir) {
        this.rootDir = rootDir;
        this.rowDir = rowDir;
        this.rootColDir = rootColDir;
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


    public void writeRow(int rowKey, String[] values) {
        String contents = getRowVal(values);
        String rowLocation = getRowLocation(rowKey);
        writeFile(rowLocation, contents);
    }


    public List<String> readRow(String key) {
        String rowLoc = getRowLocation(key);
        List<String> list = new ArrayList<>(Parser.LINE_LENGTH);
        getLineStream(rowLoc).forEach(list::add);
        return list;
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

    public void writeKeyFile(String contents) {
        writeFile(rowKeyLoc, contents);
    }

    public Stream<String> readRowKeys() {
        return getLineStream(rowKeyLoc);
    }

    private String getColDir(String colName) {
        return rootColDir + pathDelim + colName;
    }

    private void writeFile(String location, String contents) {
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


    private static final boolean isWindows() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().contains("windows");
    }


    public static Stream<String> getLineStream(String fileLocation) {
        try {
            return Files.lines(Paths.get(fileLocation));
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty();
        }
    }

}
