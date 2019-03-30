package matching;

public interface Skeleton {
    interface Composite extends Skeleton {
        Skeleton car();
        Skeleton cdr();

    }

    interface Atom {
        Expression toAtom();
    }

    interface SkeletonEvaluation extends Skeleton {
        MatchedVariable variable();
    }

    default boolean isAtom() {
        return this instanceof Atom;
    }

    default boolean isSkeletonEvaluation() {
        return this instanceof SkeletonEvaluation;
    }

}
