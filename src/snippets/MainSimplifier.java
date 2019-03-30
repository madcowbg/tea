package snippets;

import expressions.Op;
import matching.*;

import static matching.Operations.instantiate;
import static matching.Operations.match;

public class MainSimplifier {


    public static void main(String[] args) {
        var n5 = new SimpleAlgebra.Number(5);
        var n7 = new SimpleAlgebra.Number(7);
        var exp = new SimpleAlgebra.TripletExpression(Op.plus, n5, n7);

//        var pat = new SimpleCompositePattern(n5, n7);
        MatchedVariable x = new SimpleMatchedVariable("x");
        var patArbExp = new SimplePattern.SimpleArbitraryExpression(x);
        var pat = new SimplePattern.TripletPattern(Op.plus, patArbExp, n7);

        System.out.println(exp);
        System.out.println(pat);

        var emptyDict = SimpleDictionary.EMPTY;

        var dict = match(pat, exp, emptyDict);
        System.out.println(dict);

        var n3 = new SimpleAlgebra.Number(3);
        var sv = new SimpleSkeletonEvaluation(x);
        var s = new TripletSkeleton(Op.div, n3, sv);
        System.out.println(s);

        var instantiated = dict.map(d -> instantiate(d, s));
        System.out.println(instantiated);
    }

    private static class TripletSkeleton extends Cons<Skeleton, Skeleton> implements Skeleton.Composite {
        public TripletSkeleton(Op operator, Skeleton a, Skeleton b) {
            super(new OperatorSkeleton(operator), new ParametersSkeleton(a, b));
        }

        @Override
        public boolean isAtom() {
            return false;
        }
    }

    private static class OperatorSkeleton implements Skeleton, Expression.Atom {
        private final Op operator;

        public OperatorSkeleton(Op operator) {
            this.operator = operator;
        }

        @Override
        public boolean isAtomEqual(Atom exp) {
            throw new UnsupportedOperationException("can't compare skeleton and expression!");
        }

        @Override
        public boolean isExpressionEqual(Expression expression) {
            throw new UnsupportedOperationException("can't compare skeleton and expression!");
        }

        @Override
        public Expression.Composite consWith(Expression other) {
            return new SimpleAlgebra.TripletExpression(new SimpleAlgebra.OperatorExpression(operator), (SimpleAlgebra.ParametersExpression)other);
        }

        @Override
        public boolean isConstant() {
            throw new UnsupportedOperationException("can't compare skeleton and expression!");
        }

        @Override
        public boolean isAtom() {
            return true;
        }

        @Override
        public String toString() {
            return operator.print();
        }
    }

    private static class ParametersSkeleton extends Cons<Skeleton, Skeleton> implements Skeleton.Composite {
        public ParametersSkeleton(Skeleton a, Skeleton b) {
            super(a, b);
        }

        @Override
        public boolean isAtom() {
            return false;
        }
    }

    private static class SimpleSkeletonEvaluation implements Skeleton.SkeletonEvaluation {

        private final MatchedVariable var;

        public SimpleSkeletonEvaluation(MatchedVariable var) {
            this.var = var;
        }

        @Override
        public MatchedVariable variable() {
            return var;
        }

        @Override
        public String toString() {
            return "(: " + var.toString() + ")";
        }

        @Override
        public boolean isAtom() {
            return false;
        }
    }
}
