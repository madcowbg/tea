package snippets;

import matching.Operations;
import matching.Operator;

import java.util.List;

public class ProbabilisticRules<Op extends Operator, Symbol> extends Rules<Op> {
    public final Symbol Exp;
    public final Symbol Var;

    public ProbabilisticRules(Rules<Op> rules, Symbol Exp, Symbol Var) {
        super(rules.sum, rules.prod, rules.sign);
        this.Exp = Exp;
        this.Var = Var;
    }

    public final List<Operations.Rule<Object, Object>> DISTRIBUTION_SIMPLIFICATION_RULES() {
        return List.of(
                new Operations.Rule<>(
                        List.of(prod, List.of("?d", "dist"), List.of("?c", "m")),
                        List.of(prod, List.of(":", "m"), List.of(":", "dist"))),
                new Operations.Rule<>(
                        List.of(sum, List.of("?d", "dist"), List.of("?c", "a")),
                        List.of(sum, List.of(":", "a"), List.of(":", "dist"))));
    };

    public final List<Operations.Rule<Object, Object>> EXPECTATION_RULES() {
        return List.of(
                new Operations.Rule<>(
                        List.of(Exp, List.of(sum, List.of("?", "a"), List.of("?", "b"))),
                        List.of(sum, List.of(Exp, List.of(":", "a")), List.of(Exp, List.of(":", "b")))),
                new Operations.Rule<>(
                        List.of(Exp, List.of("?c", "c")),
                        List.of(":", "c")),
                new Operations.Rule<>(
                        List.of(Exp, List.of(prod, List.of("?c", "const"), List.of("?", "rest"))),
                        List.of(prod, List.of(":", "const"), List.of(Exp, List.of(":", "rest")))),
                new Operations.Rule<>(
                        List.of(Exp, List.of(prod, List.of("?", "rest"), List.of("?c", "const"))),
                        List.of(prod, List.of(":", "const"), List.of(Exp, List.of(":", "rest")))),
                new Operations.Rule<>(
                        List.of(Exp,
                                List.of(prod,
                                        List.of(prod, List.of("?c", "A"), List.of("?", "restA")), List.of("?", "B"))),
                        List.of(prod,
                                List.of(":", "A"),
                                List.of(Exp,
                                        List.of(prod, List.of(":", "restA"), List.of(":", "B"))))),
                new Operations.Rule<>(
                        List.of(Exp, List.of(prod,
                                List.of("?", "x"),
                                List.of(prod, List.of("?c", "mulY"), List.of("?", "z")))),
                        List.of(prod,
                                List.of(":", "mulY"),
                                List.of(Exp,
                                        List.of(prod, List.of(":", "x"), List.of(":", "z")))))
        );
    }

    public final List<Operations.Rule<Object, Object>> VARIANCE_RULES() {
        return List.of(
                new Operations.Rule<>(
                        List.of(Var, List.of("?", "v")),
                        List.of(Exp, List.of(prod,
                                List.of(sum, List.of(":", "v"), List.of(prod, -1, List.of(Exp, List.of(":", "v")))),
                                List.of(sum, List.of(":", "v"), List.of(prod, -1, List.of(Exp, List.of(":", "v"))))))));
    }

}
