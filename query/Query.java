package query;

import java.util.HashSet;
import java.util.Set;

public class Query {
    private Set<String> filters;
    private Set<String> selects;
    private Set<String> orders;

    public Set<String> getOrders() {
        if (orders == null) {
            orders = new HashSet<>();
        }
        return orders;
    }

    public void setOrders(Set<String> orders) {
        this.orders = orders;
    }

    public Set<String> getSelects() {
        if (selects == null) {
            selects = new HashSet<>();
        }
        return selects;
    }

    public void setSelects(Set<String> selects) {
        this.selects = selects;
    }

    public Set<String> getFilters() {
        if (filters == null) {
            filters = new HashSet<>();
        }
        return filters;
    }

    public void setFilters(Set<String> filters) {
        this.filters = filters;
    }
}
