package matching;

public interface Skeleton {
    interface Composite extends Skeleton {
        Skeleton car();
        Skeleton cdr();

    }

    interface SkeletonEvaluation extends Skeleton {
        MatchedVariable variable();
    }

    boolean isAtom();

    default boolean isSkeletonEvaluation() {
        return this instanceof SkeletonEvaluation;
    }

}
