package parser;

public class FloatColumn extends Column {
    @Override
    public Integer convert(String value) {
        return Converters.getFloatConverter().convert(value);
    }
}
