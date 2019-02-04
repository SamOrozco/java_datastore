package query.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AdvancedExpression implements Expression {
    private Expression left;
    private Expression right;
    private Operator operator;

    public AdvancedExpression(Expression left, Expression right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public Expression getLeft() {
        return left;
    }

    public void setLeft(Expression left) {
        this.left = left;
    }

    public Expression getRight() {
        return right;
    }

    public void setRight(Expression right) {
        this.right = right;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public Set<String> eval() {
        Collection<String> leftList = null;
        Collection<String> rightList = null;
        if (left != null) {
            leftList = left.eval();
        } else {
            leftList = new ArrayList<>();
        }

        if (right != null) {
            rightList = right.eval();
        } else {
            rightList = new ArrayList<>();
        }

        if (operator == Operator.AND) {
            // retain only keeps items that are in both lists
            leftList.retainAll(rightList);
        } else {
            leftList.addAll(rightList);
        }
        return new HashSet<>(leftList);
    }

    @Override
    public void print() {
        System.out.print("<");
        if (left != null) {
            left.print();
        }
        if (operator != null) {
            if (operator == Operator.AND) {
                System.out.print(" and ");
            } else {
                System.out.print(" or ");
            }
        }
        if (right != null) {
            right.print();
        }
        System.out.print(">");
    }
}
