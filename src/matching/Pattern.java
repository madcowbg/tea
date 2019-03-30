package matching;

public interface Pattern {

    interface ArbitraryConstant {
        MatchedVariable variable();
    }

    interface ArbitraryVariable {
        MatchedVariable variable();
    }

    interface ArbitraryExpression {
        MatchedVariable variable();
    }

    interface Composite extends Pattern {
        Pattern car();

        Pattern cdr();
    }

    boolean isAtom();

    default boolean isArbitraryConstant() {
        return this instanceof ArbitraryConstant;
    }

    default boolean isArbitraryVariable() {
        return this instanceof ArbitraryVariable;
    }

    default boolean isArbitraryExpression() {
        return this instanceof ArbitraryExpression;
    }

    default boolean isCompositePattern() {
        return this instanceof Composite;
    }
}
