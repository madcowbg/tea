package matching.simple;

import expressions.Op;
import matching.Cons;
import matching.Expression;
import matching.MatchedVariable;
import matching.Skeleton;

public class SimpleSkeleton {
    public static class TripletSkeleton extends Cons<Skeleton, Skeleton> implements Skeleton.Composite {
        public TripletSkeleton(Op operator, Skeleton a, Skeleton b) {
            super(new OperatorSkeleton(operator), new ParametersSkeleton(a, b));
        }

        @Override
        public boolean isAtom() {
            return false;
        }
    }

    private static class OperatorSkeleton implements Skeleton, Skeleton.Atom {
        private final Op operator;

        public OperatorSkeleton(Op operator) {
            this.operator = operator;
        }

        @Override
        public boolean isAtom() {
            return true;
        }

        @Override
        public String toString() {
            return operator.print();
        }

        @Override
        public Expression toAtom() {
            return new SimpleAlgebra.OperatorExpression(operator);
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

    public static class SimpleSkeletonEvaluation implements Skeleton.SkeletonEvaluation {

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
