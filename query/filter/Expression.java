package query.filter;

import query.Row;

import java.util.Collection;

public interface Expression {
    Collection<Row> eval();
}
