package parser;

public class TimeColumn extends Column {
    @Override
    public Integer convert(String value) {
        return Converters.getTimeConverter().convert(value);
    }
}
