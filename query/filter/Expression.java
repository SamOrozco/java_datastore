package query.filter;

import java.util.Collection;

public interface Expression {
    // returns a collection of row keys
    Collection<String> eval();
}
