package matching.simple;

import matching.Operations;
import matching.Operator;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GaussianRules<Op extends Operator, Symbol> extends ProbabilisticRules<Op, Symbol> {
    public static final Function<String, SimpleAlgebra.Distribution> PARSE_DISTRIBUTION =
            Arrays.stream(SimpleAlgebra.Distribution.values()).collect(Collectors.toMap(SimpleAlgebra.Distribution::toString, Function.identity()))::get;

    public GaussianRules(Op[] ops, Symbol Exp, Symbol Var) {
        super(ops, Exp, Var, List.of(PARSE_DISTRIBUTION));
    }

    public final List<Operations.Rule<Object, Object>> GAUSSIAN_DISTRIBUTION_INCORPORATE_LINEAR_COMBINATION_RULES() {
        return List.of(
                rulesReader.apply("(* (?c m) (N (? v) (? mu) (? sigma)))", "(N (* (sgn (: m)) (: v)) (* (: m) (: mu)) (* (* (sgn (: m)) (: m)) (: sigma)))"),
                rulesReader.apply("(+ (?c m) (N (? v) (? mu) (? sigma)))", "(N (: v) (+ (: m) (: mu)) (: sigma))"));
    }

    public final List<Operations.Rule<Object, Object>> GAUSSIAN_EXPECTATION_RULES() {
        return List.of(
                rulesReader.apply("(Exp (N (? v) (?c mu) (?c sigma)))", "(: mu)"),
                rulesReader.apply("(Exp (* (N (? v) 0 1) (N (? var) 0 1)))", "1.0"));
    }
}
