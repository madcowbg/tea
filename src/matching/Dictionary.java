package matching;

import functional.Maybe;

public interface Dictionary<Expression> {
    Maybe<Dictionary<Expression>> extend(ExpressionSystem<?, Expression, ? extends Expression> e, MatchedVariable var, Expression exp);
    Expression lookup(MatchedVariable var);
}
