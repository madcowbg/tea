package matching;

public interface PatternSystem<Atom, Pattern, CompositePattern extends Pattern> extends ExpressionSystem<Atom, Pattern, CompositePattern> {
    boolean isArbitraryConstant(Pattern p);
    boolean isArbitraryVariable(Pattern p);
    boolean isArbitraryExpression(Pattern p);
    boolean isArbitraryDistribution(Pattern p);
    boolean isCompositePattern(Pattern p);

    MatchedVariable variableFrom(Pattern p);
}
