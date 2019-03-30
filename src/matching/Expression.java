package matching;

public interface Expression {
    interface Atom {
        boolean isAtomEqual(Atom exp);
    }

    interface Constant extends Atom, Expression {

    }

    interface Variable extends Atom, Expression {

    }

    interface Composite extends Expression {
        Expression car();

        Expression cdr();

        default boolean isExpressionEqual(Expression other) {
            return this == other || (other instanceof Composite
                    && car().isExpressionEqual(((Composite) other).car())
                    && cdr().isExpressionEqual(((Composite) other).cdr()));
        }
    }

    boolean isExpressionEqual(Expression expression);

    Composite consWith(Expression other);

    default boolean isAtom() {
        return this instanceof Atom;
    }

    default boolean isConstant() {
        return this instanceof Constant;
    }

    default boolean isVariable() {
        return this instanceof Variable;
    }

    default boolean isCompositeExpression() {
        return this instanceof Composite;
    }
}
