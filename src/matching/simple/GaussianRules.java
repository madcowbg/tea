package matching.simple;

import matching.Operations;
import matching.Operator;

import java.util.List;

import static matching.simple.SimpleAlgebra.Distribution.Normal;

public class GaussianRules<Op extends Operator, Symbol> extends ProbabilisticRules<Op, Symbol> {

    public GaussianRules(ProbabilisticRules<Op, Symbol> rules) {
        super(rules, rules.Exp, rules.Var);
    }

    public final List<Operations.Rule<Object, Object>> GAUSSIAN_DISTRIBUTION_INCORPORATE_LINEAR_COMBINATION_RULES() {
        return List.of(
                new Operations.Rule<>(
                        prod.of(List.of("?c", "m"), List.of(Normal, List.of("?", "v"), List.of("?", "mu"), List.of("?", "sigma"))),
                        List.of(Normal,
                                prod.of(sign.of(List.of(":", "m")), List.of(":", "v")),
                                prod.of(List.of(":", "m"), List.of(":", "mu")),
                                prod.of(prod.of(sign.of(List.of(":", "m")), List.of(":", "m")), List.of(":", "sigma")))),
                new Operations.Rule<>(
                        sum.of(List.of("?c", "a"), List.of(Normal, List.of("?", "v"), List.of("?", "mu"), List.of("?", "sigma"))),
                        List.of(Normal, List.of(":", "v"), sum.of(List.of(":", "a"), List.of(":", "mu")), List.of(":", "sigma"))));
    }

    public final List<Operations.Rule<Object, Object>> GAUSSIAN_EXPECTATION_RULES() {
        return List.of(
                new Operations.Rule<>(
                        List.of(Exp, List.of(Normal, List.of("?", "v"), List.of("?c", "mu"), List.of("?c", "sigma"))),
                        List.of(":", "mu")),
                new Operations.Rule<>( // expectation of square of standard normal distribution
                        List.of(Exp, prod.of(
                                List.of(Normal, List.of("?", "v"), 0.0, 1.0),
                                List.of(Normal, List.of("?", "v"), 0.0, 1.0))),
                        1.0)
        );
    }
}
