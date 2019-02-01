package parser;

public class DateColumn extends Column {
    public DateColumn() {
    }

    @Override
    public Integer convert(String value) {
        return Converters.getDateConverter().convert(value);
    }
}
