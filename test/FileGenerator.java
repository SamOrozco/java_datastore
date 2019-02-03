package test;

import parser.FileDataStore;

public class FileGenerator {
    //    public static final int STB_INDEX = 0;
    //    public static final int TITLE_INDEX = 1;
    //    public static final int PROVIDER_INDEX = 2;
    //    public static final int DATE_INDEX = 3;
    //    public static final int REV_INDEX = 4;
    //    public static final int VIEW_TIME_INDEX = 5;
    static String[] titles = new String[]{
        "the hobbit",
        "spider man",
        "harry potter",
        "lord of the rings",
        "the night before christmas",
        "friday night lights",
        "shrek",
        "princess bride",
        "holes"
    };

    static String[] providers = new String[]{
        "warner bros",
        "netflex",
        "hulu",
        "amazon prime",
        "netflex",
        "tv"
    };
    static String[] dates = new String[]{"2014-04-10", "2014-06-20", "2014-12-10"};
    static String[] revs = new String[]{"6.0", "8.10", "9.0", "12", "15.5", "12.1", "120", "20"};
    static String[] viewTimes = new String[]{"1:00", "2:20", "9:00"};


    public static void main(String[] args) {
        int num = 1000;
        StringBuilder builder = new StringBuilder();
        builder.append("STB").append("|")
               .append("TITLE").append("|")
               .append("PROVIDER").append("|")
               .append("DATE").append("|")
               .append("REV").append("|")
               .append("VIEW_TIME").append("\r\n");
        for (int i = 0; i < num; i++) {
            String title = titles[((int) (Math.random() * 100)) % titles.length];
            String provider = providers[((int) (Math.random() * 100)) % providers.length];
            String date = dates[((int) (Math.random() * 100)) % dates.length];
            String rev = revs[((int) (Math.random() * 100)) % revs.length];
            String viewTime = viewTimes[((int) (Math.random() * 100)) % viewTimes.length];
            String stb = String.format("stb%d", i);
            builder.append(stb).append("|")
                   .append(title).append("|")
                   .append(provider).append("|")
                   .append(date).append("|")
                   .append(rev).append("|")
                   .append(viewTime).append("\r\n");
        }

        FileDataStore store = new FileDataStore();
        store.storeContents(String.format("./files/test%d.txt", num), builder.toString());
    }
}
