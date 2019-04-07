package snippets;

import expressions.Op;
import matching.Operations;

import java.util.List;

import static matching.simple.SimpleAlgebra.Distribution.Normal;

public class GaussianRules {

    public final static List<Operations.Rule<Object, Object>> GAUSSIAN_DISTRIBUTION_INCORPORATE_LINEAR_COMBINATION_RULES = List.of(
            new Operations.Rule<>(
                    List.of(Op.mul, List.of("?c", "m"), List.of(Normal, List.of("?", "v"), List.of("?", "mu"), List.of("?", "sigma"))),
                    List.of(Normal,
                            List.of(Op.mul, List.of(Op.sign, List.of(":", "m")), List.of(":", "v")),
                            List.of(Op.mul, List.of(":", "m"), List.of(":", "mu")),
                            List.of(Op.mul, List.of(Op.mul, List.of(Op.sign, List.of(":", "m")), List.of(":", "m")), List.of(":", "sigma")))),
            new Operations.Rule<>(
                    List.of(Op.plus, List.of("?c", "a"), List.of(Normal, List.of("?", "v"), List.of("?", "mu"), List.of("?", "sigma"))),
                    List.of(Normal, List.of(":", "v"), List.of(Op.plus, List.of(":", "a"), List.of(":", "mu")), List.of(":", "sigma"))));

    public final static List<Operations.Rule<Object, Object>> GAUSSIAN_EXPECTATION_RULES(Object Exp) {
        return List.of(
                new Operations.Rule<>(
                        List.of(Exp, List.of(Normal, List.of("?", "v"), List.of("?c", "mu"), List.of("?c", "sigma"))),
                        List.of(":", "mu")),
                new Operations.Rule<>( // expectation of square of standard normal distribution
                        List.of(Exp, List.of(Op.mul,
                                List.of(Normal, List.of("?", "v"), 0.0, 1.0),
                                List.of(Normal, List.of("?", "v"), 0.0, 1.0))),
                        1.0)
        );
    }
}
