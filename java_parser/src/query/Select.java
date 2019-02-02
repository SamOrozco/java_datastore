package query;

public class Select {
    private boolean stb;
    private boolean title;
    private boolean provider;
    private boolean date;
    private boolean rev;
    private boolean viewTime;

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
            if (val.trim().equalsIgnoreCase("stb")) stb = true;
            if (val.trim().equalsIgnoreCase("title")) title = true;
            if (val.trim().equalsIgnoreCase("provider")) provider = true;
            if (val.trim().equalsIgnoreCase("date")) date = true;
            if (val.trim().equalsIgnoreCase("rev")) rev = true;
            if (val.trim().equalsIgnoreCase("viewTime")) viewTime = true;
        }
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
}
