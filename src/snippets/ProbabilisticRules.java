package snippets;

import expressions.Op;
import matching.Operations;

import java.util.List;

import static matching.simple.SimpleAlgebra.Distribution.Normal;

public class ProbabilisticRules {

    public final static List<Operations.Rule<Object, Object>> DISTRIBUTION_SIMPLIFICATION_RULES = List.of(
            new Operations.Rule<>(
                    List.of(Op.mul, List.of("?d", "dist"), List.of("?c", "m")),
                    List.of(Op.mul, List.of(":", "m"), List.of(":", "dist"))),
            new Operations.Rule<>(
                    List.of(Op.plus, List.of("?d", "dist"), List.of("?c", "a")),
                    List.of(Op.plus, List.of(":", "a"), List.of(":", "dist"))));

    public final static List<Operations.Rule<Object, Object>> EXPECTATION_RULES(Object Exp) {
        return List.of(
                new Operations.Rule<>(
                        List.of(Exp, List.of(Op.plus, List.of("?", "a"), List.of("?", "b"))),
                        List.of(Op.plus, List.of(Exp, List.of(":", "a")), List.of(Exp, List.of(":", "b")))),
                new Operations.Rule<>(
                        List.of(Exp, List.of("?c", "c")),
                        List.of(":", "c")),
                new Operations.Rule<>(
                        List.of(Exp, List.of(Op.mul, List.of("?c", "const"), List.of("?", "rest"))),
                        List.of(Op.mul, List.of(":", "const"), List.of(Exp, List.of(":", "rest")))),
                new Operations.Rule<>(
                        List.of(Exp, List.of(Op.mul, List.of("?", "rest"), List.of("?c", "const"))),
                        List.of(Op.mul, List.of(":", "const"), List.of(Exp, List.of(":", "rest")))),
                new Operations.Rule<>(
                        List.of(Exp,
                                List.of(Op.mul,
                                        List.of(Op.mul, List.of("?c", "A"), List.of("?", "restA")), List.of("?", "B"))),
                        List.of(Op.mul,
                                List.of(":", "A"),
                                List.of(Exp,
                                        List.of(Op.mul, List.of(":", "restA"), List.of(":", "B"))))),
                new Operations.Rule<>(
                        List.of(Exp, List.of(Op.mul,
                                List.of("?", "x"),
                                List.of(Op.mul, List.of("?c", "mulY"), List.of("?", "z")))),
                        List.of(Op.mul,
                                List.of(":", "mulY"),
                                List.of(Exp,
                                        List.of(Op.mul, List.of(":", "x"), List.of(":", "z")))))
        );
    }

    public final static List<Operations.Rule<Object, Object>> VARIANCE_RULES(Object Exp, Object Var) {
        return List.of(
                new Operations.Rule<>(
                        List.of(Var, List.of("?", "v")),
                        List.of(Exp, List.of(Op.mul,
                                List.of(Op.plus, List.of(":", "v"), List.of(Op.mul, -1, List.of(Exp, List.of(":", "v")))),
                                List.of(Op.plus, List.of(":", "v"), List.of(Op.mul, -1, List.of(Exp, List.of(":", "v"))))))));
    }

}
