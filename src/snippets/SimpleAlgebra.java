package snippets;

import matching.Cons;
import matching.Expression;
import matching.Pattern;

class SimpleAlgebra {
    static final double TOL = 1e-12;

    static class SimpleNumber implements Expression.Atom, Expression.Constant, Pattern, MainSimplifier.Skeleton {
        private final double value;

        SimpleNumber(double value) {
            this.value = value;
        }

        @Override
        public boolean isAtomEqual(Atom other) {
            return this.isExpressionEqual(other);
        }

        @Override
        public boolean isExpressionEqual(Expression other) {
            return other == this || (other instanceof SimpleNumber && Math.abs(this.value - ((SimpleNumber) other).value) < TOL);
        }

        @Override
        public Expression.Composite consWith(Expression other) {
            return new SimpleCompositeExpression(this, other);
        }

        @Override
        public boolean isAtom() {
            return true;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    static class SimpleCompositeExpression extends Cons<Expression, Expression> implements Expression.Composite {
        public SimpleCompositeExpression(Expression head, Expression tail) {
            super(head, tail);
        }

        @Override
        public Composite consWith(Expression other) {
            return new SimpleCompositeExpression(this, other);
        }

    }

}
