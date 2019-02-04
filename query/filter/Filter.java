package query.filter;

import parser.RowStore;
import query.Select;

import java.util.*;
import java.util.regex.Pattern;

public class Filter implements Expression {
    private static String delim = "=";
    private Map<String, String> filterMap = new HashMap<>();
    private static final String unq = "||";
    private static final String and = " and ";
    private static final String andUnq = unq + "and" + unq;
    private static final String or = " or ";
    private static final String orUnq = unq + "or " + unq;
    private static final String openParen = "(";
    private static final String openParenUnq = unq + "(" + unq;
    private static final String closeParen = ")";
    private static final String closeParenUnq = unq + ")" + unq;
    private final RowStore store;
    private final Select select;
    private Expression root;
    public boolean explain = false;
    //    STB="stb1" AND TITLE="the hobbit" OR TITLE="unbreakable"

    public Filter(String filter, RowStore store, Select select) {
        this.store = store;
        this.select = select;
        parse(filter);
    }


    public void parse(String filter) {
        if (filter == null || filter.isEmpty()) {
            return;
        }
        if (isAdvanced(filter)) {
            root = parseAdvanced(filter);
        } else {
            root = parseFilter(filter);
        }
    }


    private Expression parseAdvanced(String filter) {
        filter = filter.toLowerCase();
        String repl = filter.replace(and, andUnq);
        repl = repl.replace(or, orUnq);
        repl = repl.replace(openParen, openParenUnq);
        repl = repl.replace(closeParen, closeParenUnq);
        String[] tokens = repl.split(Pattern.quote(unq));
        return readTokens(tokens, root);
    }


    private Expression readTokens(String[] tokens, Expression expression) {
        Expression current = expression;
        Operator operator = null;
        for (int i = 0; i < tokens.length; i++) {

            String val = tokens[i];
            if (val == null || val.isEmpty()) {
                continue;
            } else {
                val = val.trim().toLowerCase();
            }
            if (val.equals(openParen)) {
                String[] parenToks = findTokens(i + 1, tokens);
                // increase by read count
                // add one to escape the close paren
                i += parenToks.length + 1;
                Expression expr = readTokens(parenToks, null);
                if (current == null) { // first expression had parens
                    current = expr;
                    continue;
                }
                // else combine our expression
                current = new AdvancedExpression(current, expr, operator);
                continue;
            }
            if (current == null) {
                // we assume first token is expresion
                current = parseFilter(val);
                continue;
            }
            Operator op = getOperator(val);
            boolean isOp = op != null;
            if (!isOp && operator == null) { // malformed query
                throw new RuntimeException("malformed query expected operator");
            } else if (operator == null) {
                operator = op;
                continue;
            }

            if (isAdvanced(val)) {
                expression =
                    new AdvancedExpression(current, parseAdvanced(cleanseEdges(val)), operator);
            } else {
                expression = new AdvancedExpression(current, parseFilter(val), operator);
            }
            current = expression;
            operator = null;
        }
        return current;
    }


    private String[] findTokens(int index, String[] args) {
        List<String> vals = new ArrayList<>();
        for (int i = index; i < args.length; i++) {
            String val = args[i];
            if (val.equals(closeParen) || val.isEmpty()) {
                return vals.toArray(new String[0]);
            }
            vals.add(args[i]);
        }
        throw new RuntimeException("malformed query no closing parenthesis");
    }


    private String cleanseEdges(String val) {
        int length = val.length();
        if (length < 2) {
            return val;
        }

        if (endsContain(val, '"', '"') ||
            endsContain(val, '(', ')') ||
            endsContain(val, '\'', '\'')) {
            return trimEdges(val);
        }
        return val;
    }


    private boolean endsContain(String val, Character left, Character right) {
        return val.charAt(0) == left && val.charAt(val.length() - 1) == right;
    }


    private String trimEdges(String val) {
        return val.substring(1, val.length() - 2);
    }

    private Operator getOperator(String val) {
        boolean andOp = val.trim().toLowerCase().equals(and.trim());
        boolean orOp = val.trim().toLowerCase().equals(or.trim());
        if (andOp) {
            return Operator.AND;
        }

        if (orOp) {
            return Operator.OR;
        }
        return null;
    }

    /**
     * This method assumes a single expression String
     * Date=2014-04-12 || TITLE="the hobbit"
     *
     * @param filter
     * @return
     */
    private Expression parseFilter(String filter) {
        String[] segments = filter.split(delim);
        if (segments.length != 2) {
            return null;
        }
        return new SingleExpression(segments[0],
                                    segments[1],
                                    store,
                                    select);
    }

    /**
     * If filter contains ' or ' or ' and ' we say it is advanced
     *
     * @param filter
     * @return
     */
    private boolean isAdvanced(String filter) {
        String lower = filter.toLowerCase();
        return lower.contains(and) || lower.contains(or);
    }


    /**
     * If filter contains ' or ' or ' and ' we say it is advanced
     *
     * @param filter
     * @return
     */
    private boolean isGrouped(String filter) {
        return filter.contains("(") || filter.contains(")");
    }

    private void addFilter(String colName, String value) {
        this.getFilterMap().put(colName, value);
    }


    private boolean validColumnName(String colName) {
        if (colName == null || colName.isEmpty()) {
            return false;
        }
        return true;
    }

    public Map<String, String> getFilterMap() {
        return filterMap;
    }

    public void setFilterMap(Map<String, String> filterMap) {
        this.filterMap = filterMap;
    }

    @Override
    public Collection<String> eval() {
        if (explain) {
            System.out.println("EXPLAIN:");
            this.print();
        }
        if (this.root == null) {
            return store.getRowKeys();
        }
        return root.eval();
    }

    @Override
    public void print() {
        if (this.root != null) {
            this.root.print();
        }
    }
}
