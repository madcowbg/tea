package matching;

import functional.Maybe;

public interface Dictionary {
    Maybe<Dictionary> extend(Pattern.ArbitraryConstant pat, Expression.Constant exp);

    Maybe<Dictionary> extend(Pattern.ArbitraryVariable pat, Expression.Variable exp);

    Maybe<Dictionary> extend(Pattern.ArbitraryExpression pat, Expression exp);

    Expression lookup(MatchedVariable var);
}
