package parser;

public class TextColumn extends Column {
    @Override
    public Integer convert(String value) {
        return Converters.getStringConverter().convert(value);
    }
}
