package query;

import java.util.HashMap;
import java.util.Map;

import static parser.Parser.*;

public class Select {
    private boolean stb;
    private boolean title;
    private boolean provider;
    private boolean date;
    private boolean rev;
    private boolean viewTime;
    private boolean advancedSearch;
    private Map<Integer, String> columnAdvancedSelect;

    public Select(String value) {
        if (value == null) {
            allTrue();
            return;
        }
        String[] values = value.trim().split(",");
        if (value.length() < 1) {
            allTrue();
            return;
        }
        for (String val : values) {
            if (val.trim().toLowerCase().contains("stb")) stb = true;
            if (val.trim().toLowerCase().contains("title")) title = true;
            if (val.trim().toLowerCase().contains("provider")) provider = true;
            if (val.trim().toLowerCase().contains("date")) date = true;
            if (val.trim().toLowerCase().contains("rev")) rev = true;
            if (val.trim().toLowerCase().contains("viewTime")) viewTime = true;
            // if advanced select we want to know that
            boolean tempSearch = false;
            if ((tempSearch = advancedSelect(value))) {
                String[] segments = val.split(":");
                if (segments.length != 2) {
                    tempSearch = false;
                    continue;
                }
                if (stb) getColumnAdvancedSelect().put(STB_INDEX, segments[1]);
                if (title) getColumnAdvancedSelect().put(TITLE_INDEX, segments[1]);
                if (provider) getColumnAdvancedSelect().put(PROVIDER_INDEX, segments[1]);
                if (date) getColumnAdvancedSelect().put(DATE_INDEX, segments[1]);
                if (rev) getColumnAdvancedSelect().put(REV_INDEX, segments[1]);
                if (viewTime) getColumnAdvancedSelect().put(VIEW_TIME_INDEX, segments[1]);
            }

            if (!advancedSearch && tempSearch) {
                advancedSearch = tempSearch;
            }
        }
    }


    private boolean advancedSelect(String value) {
        return value.contains(":");
    }

    private void allTrue() {
        stb = true;
        title = true;
        provider = true;
        date = true;
        rev = true;
        viewTime = true;
    }

    public boolean isStb() {
        return stb;
    }

    public void setStb(boolean stb) {
        this.stb = stb;
    }

    public boolean isTitle() {
        return title;
    }

    public void setTitle(boolean title) {
        this.title = title;
    }

    public boolean isProvider() {
        return provider;
    }

    public void setProvider(boolean provider) {
        this.provider = provider;
    }

    public boolean isDate() {
        return date;
    }

    public void setDate(boolean date) {
        this.date = date;
    }

    public boolean isRev() {
        return rev;
    }

    public void setRev(boolean rev) {
        this.rev = rev;
    }

    public boolean isViewTime() {
        return viewTime;
    }

    public void setViewTime(boolean viewTime) {
        this.viewTime = viewTime;
    }

    public boolean isAdvancedSearch() {
        return advancedSearch;
    }

    public void setAdvancedSearch(boolean advancedSearch) {
        this.advancedSearch = advancedSearch;
    }

    public Map<Integer, String> getColumnAdvancedSelect() {
        if (columnAdvancedSelect == null) {
            columnAdvancedSelect = new HashMap<>();
        }
        return columnAdvancedSelect;
    }

    public void setColumnAdvancedSelect(
        Map<Integer, String> columnAdvancedSelect) {
        this.columnAdvancedSelect = columnAdvancedSelect;
    }
}
