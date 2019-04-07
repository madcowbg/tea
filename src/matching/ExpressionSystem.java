package matching;

public interface ExpressionSystem<Atom, Expression, CompositeExpression extends Expression> {
    Expression car(CompositeExpression exp);
    Expression cdr(CompositeExpression exp);

    boolean isAtomEqual(Atom a, Atom b);

    CompositeExpression cons(Expression a, Expression b);

    boolean isAtom(Expression a);
    boolean isConstant(Expression a);
    boolean isVariable(Expression a);
    boolean isDistribution(Expression exp);

    boolean isCompositeExpression(Expression a);

    boolean isExpressionEqual(Expression a, Expression b);

    Expression evaluate(Expression skeleton);
}
