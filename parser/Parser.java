package parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static parser.FileDataStore.getLineStream;
import static parser.FileDataStore.newLine;

public class Parser {
    private final String delim;
    private static final int MAX_CAPACITY = 1000;
    public static final int LINE_LENGTH = 6;
    FileDataStore dataStore;
    private final BlockingQueue<String> lineQueue = new LinkedBlockingQueue<>(MAX_CAPACITY);
    private final StringBuilder keys = new StringBuilder();
    private AtomicInteger readCount = new AtomicInteger(0);
    private AtomicInteger parseCount = new AtomicInteger(0);
    private List<String> headers;
    private Map<Integer, Column> columns;
    public static final int STB_INDEX = 0;
    public static final int TITLE_INDEX = 1;
    public static final int PROVIDER_INDEX = 2;
    public static final int DATE_INDEX = 3;
    public static final int REV_INDEX = 4;
    public static final int VIEW_TIME_INDEX = 5;
    public static final int[] INDEXES =
        new int[]{STB_INDEX, TITLE_INDEX, PROVIDER_INDEX, DATE_INDEX, REV_INDEX, VIEW_TIME_INDEX};

    private Parser(String delim) {
        this.delim = delim;
        initColumns();
        initFileStructure();
    }

    public static Parser newPipeParser() {
        return new Parser("|");
    }

    private void initColumns() {
        columns = new HashMap<>();
        columns.put(STB_INDEX, new TextColumn());
        columns.put(TITLE_INDEX, new TextColumn());
        columns.put(PROVIDER_INDEX, new TextColumn());
        columns.put(DATE_INDEX, new DateColumn());
        columns.put(REV_INDEX, new FloatColumn());
        columns.put(VIEW_TIME_INDEX, new TimeColumn());
    }


    private void initFileStructure() {
        this.dataStore = new FileDataStore();
    }


    public void store(String fileLocation) throws FileNotFoundException {
        File file = new File(fileLocation);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        Stream<String> lineStream = getLineStream(fileLocation);

        //read from block queue async
        try {
            CompletableFuture.runAsync(parseLinesFromQueue());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // read lines on main thread
        lineStream.forEach(line -> {
            int count = readCount.getAndIncrement();
            if (count == 0) {
                parseHeaders(line);
                return;
            }
            try {
                lineQueue.put(line);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        while ((readCount.get() - 1) != parseCount.get()) {
        }


        // write columns to disc
        for (int index : INDEXES) {
            this.dataStore.writeColumn(headers.get(index), columns.get(index));
        }

        // write keys to disc
        this.dataStore.writeKeyFile(keys.toString());

    }


    private void parse(String line, int rowCount) {
        if (line == null || line.isEmpty()) {
            return;
        }
        String[] segments = line.split(Pattern.quote(delim));
        if (segments.length != LINE_LENGTH) {
            throw new RuntimeException(String.format("invalid segment count row # %d", rowCount));
        }

        // unique row key
        int rowKey = rowKey(segments[STB_INDEX],
                            segments[TITLE_INDEX],
                            segments[DATE_INDEX]);
        // appending to our keys string
        keys.append(rowKey).append(newLine);
        // write our rows to disc with their unique key
        this.dataStore.writeRow(rowKey, segments);

        for (int index : INDEXES) {
            Column col = null;
            if ((col = columns.get(index)) != null) {
                col.addValue(segments[index], rowKey);
            }
        }
    }


    private int rowKey(String sbt, String title, String date) {
        return Objects.hash(sbt, title, date);
    }


    private void parseHeaders(String line) {
        if (line == null || line.isEmpty()) {
            return;
        }
        String[] segments = line.split(Pattern.quote(delim));
        if (segments.length < 1) {
            return;
        }
        headers = Arrays.asList(segments);
    }


    /**
     * This method is meant to run in a separate thread from the reading thread
     * The two thread communicate through the blocking queue
     *
     * @return
     */
    private Runnable parseLinesFromQueue() {
        return () -> {
            String line = null;
            int lineCount = -1;
            while (true) {
                try {
                    if ((line = lineQueue.poll(100, TimeUnit.MILLISECONDS)) == null) break;
                    lineCount = parseCount.getAndIncrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                parse(line, lineCount);
            }
        };
    }
}
