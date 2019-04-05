package matching;

public interface SkeletonSystem<Atom, Skeleton, CompositeSkeleton extends Skeleton, Expression> extends ExpressionSystem<Atom, Skeleton, CompositeSkeleton> {
//
//    interface Atom {
//        Expression toAtom();
//    }

    MatchedVariable variableFromSkeletonEvaluation(Skeleton s);
    boolean isSkeletonEvaluation(Skeleton s);

    Expression toAtom(Skeleton skeleton);
}
