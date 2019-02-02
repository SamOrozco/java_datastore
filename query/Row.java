package query;

import java.util.Comparator;
import java.util.List;

import static parser.Parser.*;

public class Row {
    private String stb;
    private String title;
    private String provider;
    private String date;
    private String rev;
    private String viewTime;


    public Row(List<String> values, Select select) {
        if (select.isStb()) {
            stb = values.get(STB_INDEX);
        }
        if (select.isTitle()) {
            title = values.get(TITLE_INDEX);
        }
        if (select.isProvider()) {
            provider = values.get(PROVIDER_INDEX);
        }
        if (select.isDate()) {
            date = values.get(DATE_INDEX);
        }
        if (select.isRev()) {
            rev = values.get(REV_INDEX);
        }
        if (select.isViewTime()) {
            viewTime = values.get(VIEW_TIME_INDEX);
        }
    }

    public String getStb() {
        return stb;
    }

    public void setStb(String stb) {
        this.stb = stb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getViewTime() {
        return viewTime;
    }

    public void setViewTime(String viewTime) {
        this.viewTime = viewTime;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (stb != null && !stb.isEmpty()) {
            builder.append(stb).append(",");
        }
        if (title != null && !title.isEmpty()) {
            builder.append(title).append(",");
        }
        if (provider != null && !provider.isEmpty()) {
            builder.append(provider).append(",");
        }

        if (date != null && !date.isEmpty()) {
            builder.append(date).append(",");
        }

        if (rev != null && !rev.isEmpty()) {
            builder.append(rev).append(",");
        }

        if (viewTime != null && !viewTime.isEmpty()) {
            builder.append(viewTime).append(",");
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }


    public static Comparator<Row> stbComp() {
        return (Row o1, Row o2) -> {
            String stb1 = o1.stb;
            String stb2 = o2.stb;
            if (stb1 == null) stb1 = "";
            if (stb2 == null) stb2 = "";
            return stb1.compareTo(stb2);
        };
    }


    public static Comparator<Row> titleComp() {
        return (Row o1, Row o2) -> {
            String stb1 = o1.title;
            String stb2 = o2.title;
            if (stb1 == null) stb1 = "";
            if (stb2 == null) stb2 = "";
            return stb1.compareTo(stb2);
        };
    }

    public static Comparator<Row> providerComp() {
        return (Row o1, Row o2) -> {
            String stb1 = o1.provider;
            String stb2 = o2.provider;
            if (stb1 == null) stb1 = "";
            if (stb2 == null) stb2 = "";
            return stb1.compareTo(stb2);
        };
    }

    public static Comparator<Row> getDateComp() {
        return (Row o1, Row o2) -> {
            String stb1 = o1.date;
            String stb2 = o2.date;
            if (stb1 == null) stb1 = "";
            if (stb2 == null) stb2 = "";
            return stb1.compareTo(stb2);
        };
    }


    public static Comparator<Row> getRevComp() {
        return (Row o1, Row o2) -> {
            String stb1 = o1.rev;
            String stb2 = o2.rev;
            if (stb1 == null) stb1 = "";
            if (stb2 == null) stb2 = "";
            return stb1.compareTo(stb2);
        };
    }


    public static Comparator<Row> getViewTimComp() {
        return (Row o1, Row o2) -> {
            String stb1 = o1.viewTime;
            String stb2 = o2.viewTime;
            if (stb1 == null) stb1 = "";
            if (stb2 == null) stb2 = "";
            return stb1.compareTo(stb2);
        };
    }
}
