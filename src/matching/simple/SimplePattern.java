package matching.simple;

import expressions.Op;
import matching.Cons;
import matching.Expression;
import matching.MatchedVariable;
import matching.Pattern;

import java.util.Objects;

public class SimplePattern {
    public static class TripletPattern extends Cons<Pattern, Pattern> implements Pattern.Composite {

        public TripletPattern(Op op, Pattern a, Pattern b) {
            super(new OperatorPattern(op), new ParametersPattern(a, b));
        }

        @Override
        public boolean isAtom() {
            return false;
        }

    }

    private static class ParametersPattern extends Cons<Pattern, Pattern> implements Pattern.Composite {
        public ParametersPattern(Pattern a, Pattern b) {
            super(a, b);
        }

        @Override
        public boolean isAtom() {
            return false;
        }
    }

    static class OperatorPattern implements Pattern, Expression.Atom {
        private final Op operator;

        public OperatorPattern(Op operator) {
            this.operator = operator;
        }

        @Override
        public boolean isAtomEqual(Expression.Atom exp) {
            return exp == this || (exp instanceof SimpleAlgebra.OperatorExpression
                    && Objects.equals(((SimpleAlgebra.OperatorExpression) exp).operator, this.operator));
        }

//        @Override
//        public Expression.Composite consWith(Expression other) {
//            return new SimpleAlgebra.TripletExpression(new SimpleAlgebra.OperatorExpression(operator), (SimpleAlgebra.ParametersExpression) other);
//        }

        @Override
        public boolean isAtom() {
            return true;
        }

        @Override
        public String toString() {
            return operator.print();
        }
    }

    public static class SimpleArbitraryExpression implements Pattern, Pattern.ArbitraryExpression {
        private final MatchedVariable var;

        public SimpleArbitraryExpression(MatchedVariable var) {
            this.var = var;
        }

        @Override
        public boolean isAtom() {
            return false;
        }

        @Override
        public MatchedVariable variable() {
            return var;
        }

        @Override
        public String toString() {
            return "(? " + var.toString() + ")";
        }
    }
}
