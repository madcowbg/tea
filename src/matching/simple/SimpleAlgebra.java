package matching.simple;

import expressions.Op;
import matching.Cons;
import matching.Expression;
import matching.Pattern;
import matching.Skeleton;

import java.util.Objects;

public class SimpleAlgebra {
    static final double TOL = 1e-12;

    public static class Number implements Expression, Expression.Atom, Expression.Constant, Pattern, Skeleton, Skeleton.Atom {
        private final double value;

        public Number(double value) {
            this.value = value;
        }

        @Override
        public boolean isAtomEqual(Expression.Atom other) {
            return other == this || (other instanceof Number && Math.abs(this.value - ((Number) other).value) < TOL);
        }

        @Override
        public boolean isExpressionEqual(Expression other) {
            return other == this || (other instanceof Number && Math.abs(this.value - ((Number) other).value) < TOL);
        }

        @Override
        public Expression.Composite consWith(Expression other) {
            return new ParametersExpression(this, other);
        }

        @Override
        public boolean isAtom() {
            return true;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public Expression toAtom() {
            return this;
        }
    }

    public static class TripletExpression extends Cons<Expression, Expression> implements Expression.Composite {
        public TripletExpression(Op operator, Expression a, Expression b) {
            this(new OperatorExpression(operator), new ParametersExpression(a, b));
        }

        TripletExpression(OperatorExpression operator, ParametersExpression params) {
            super(operator, params);
        }

        @Override
        public Composite consWith(Expression other) {
            throw new UnsupportedOperationException("can't use triplet as first operator!");
        }

        @Override
        public String toString() {
            return "(" + car().toString() + " " + cdr().toString() + ")";
        }
    }

    static class OperatorExpression implements Expression, Expression.Atom {
        final Op operator;

        OperatorExpression(Op operator) {
            this.operator = operator;
        }

        @Override
        public boolean isExpressionEqual(Expression exp) {
            return this == exp || (exp instanceof OperatorExpression && Objects.equals(((OperatorExpression) exp).operator, this.operator));
        }

        @Override
        public Composite consWith(Expression other) {
            return new TripletExpression(this, (ParametersExpression)other);
        }

        @Override
        public boolean isAtomEqual(Atom exp) {
            return this == exp || (exp instanceof OperatorExpression && Objects.equals(((OperatorExpression) exp).operator, this.operator));
        }

        @Override
        public String toString() {
            return operator.print();
        }
    }

    static class ParametersExpression extends Cons<Expression, Expression> implements Expression.Composite {
        ParametersExpression(Expression head, Expression tail) {
            super(head, tail);
        }

        @Override
        public Composite consWith(Expression other) {
            throw new UnsupportedOperationException("can't use parameters as first operator!");
        }

        @Override
        public String toString() {
            return car().toString() + " " + cdr().toString();
        }
    }
}