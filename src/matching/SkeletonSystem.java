package matching;

public interface SkeletonSystem<Atom, Skeleton, CompositeSkeleton extends Skeleton, Expression> extends ExpressionSystem<Atom, Skeleton, CompositeSkeleton> {
    MatchedVariable variableFromSkeletonEvaluation(Skeleton s);
    boolean isSkeletonEvaluation(Skeleton s);
    boolean isExpressionEvaluation(Skeleton skeleton);

    Expression toAtom(Skeleton skeleton);
    Skeleton expressionToEval(Skeleton skeleton);
}
