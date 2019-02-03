package query.filter;

import parser.RowStore;
import query.Select;

import java.util.List;

public class SingleExpression implements Expression {
    private String col;
    private String value;
    private RowStore store;
    private Select select;

    public SingleExpression(String col,
                            String value,
                            RowStore store,
                            Select select) {
        this.setCol(col);
        this.setValue(value);
        this.setStore(store);
        this.setSelect(select);
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public List<String> eval() {
        return store.getRowsFromColAndValue(col, value);
    }

    public RowStore getStore() {
        return store;
    }

    public void setStore(RowStore store) {
        this.store = store;
    }

    public Select getSelect() {
        return select;
    }

    public void setSelect(Select select) {
        this.select = select;
    }
}
