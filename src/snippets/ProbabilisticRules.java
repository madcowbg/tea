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
                        prod.of(List.of("?d", "dist"), List.of("?c", "m")),
                        prod.of(List.of(":", "m"), List.of(":", "dist"))),
                new Operations.Rule<>(
                        sum.of(List.of("?d", "dist"), List.of("?c", "a")),
                        sum.of(List.of(":", "a"), List.of(":", "dist"))));
    };

    public final List<Operations.Rule<Object, Object>> EXPECTATION_RULES() {
        return List.of(
                new Operations.Rule<>(
                        List.of(Exp, sum.of(List.of("?", "a"), List.of("?", "b"))),
                        sum.of(List.of(Exp, List.of(":", "a")), List.of(Exp, List.of(":", "b")))),
                new Operations.Rule<>(
                        List.of(Exp, List.of("?c", "c")),
                        List.of(":", "c")),
                new Operations.Rule<>(
                        List.of(Exp, prod.of(List.of("?c", "const"), List.of("?", "rest"))),
                        prod.of(List.of(":", "const"), List.of(Exp, List.of(":", "rest")))),
                new Operations.Rule<>(
                        List.of(Exp, prod.of(List.of("?", "rest"), List.of("?c", "const"))),
                        prod.of(List.of(":", "const"), List.of(Exp, List.of(":", "rest")))),
                new Operations.Rule<>(
                        List.of(Exp, prod.of(prod.of(List.of("?c", "A"), List.of("?", "restA")), List.of("?", "B"))),
                        prod.of(
                                List.of(":", "A"),
                                List.of(Exp,
                                        prod.of(List.of(":", "restA"), List.of(":", "B"))))),
                new Operations.Rule<>(
                        List.of(Exp, prod.of(
                                List.of("?", "x"),
                                prod.of(List.of("?c", "mulY"), List.of("?", "z")))),
                        prod.of(
                                List.of(":", "mulY"),
                                List.of(Exp,
                                        prod.of(List.of(":", "x"), List.of(":", "z")))))
        );
    }

    public final List<Operations.Rule<Object, Object>> VARIANCE_RULES() {
        return List.of(
                new Operations.Rule<>(
                        List.of(Var, List.of("?", "v")),
                        List.of(Exp, prod.of(
                                sum.of(List.of(":", "v"), prod.of(-1, List.of(Exp, List.of(":", "v")))),
                                sum.of(List.of(":", "v"), prod.of(-1, List.of(Exp, List.of(":", "v"))))))));
    }

}
