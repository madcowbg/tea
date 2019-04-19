package matching.simple;

import matching.Operations;
import matching.Operator;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProbabilisticRules<Op extends Operator, Symbol> extends Rules<Op> {
    public ProbabilisticRules(Op[] ops, Symbol Exp, Symbol Var) {
        this(ops, Exp, Var, Collections.emptyList());
    }

    protected ProbabilisticRules(Op[] ops, Symbol Exp, Symbol Var, List<Function<String, ?>> operatorTypeMaps) {
        super(
                ops,
                Stream.concat(
                        operatorTypeMaps.stream(),
                        Stream.of(
                                (str) -> str.equals("Var") ? Var : null,
                                (str) -> str.equals("Exp") ? Exp : null)).collect(Collectors.toList()));
    }

    public final List<Operations.Rule<Object, Object>> DISTRIBUTION_SIMPLIFICATION_RULES() {
        return List.of(
                rulesReader.apply("(* (?d dist) (?c a))", "(* (: a) (: dist))"),
                rulesReader.apply("(+ (?d dist) (?c a))", "(+ (: a) (: dist))"),
                rulesReader.apply("(* (?d dist) (* (?c a) (? e)))", "(* (: a) (* (: dist) (: e)))")
        );
    }

    public final List<Operations.Rule<Object, Object>> EXPECTATION_RULES() {
        return List.of(
                rulesReader.apply("(Exp (+ (? a) (? b)))", "(+ (Exp (: a)) (Exp (: b)))"),
                rulesReader.apply("(Exp (?c c))", "(: c)"),
                rulesReader.apply("(Exp (* (?c a) (? e)))", "(* (: a) (Exp (: e)))")
        );
    }

    public final List<Operations.Rule<Object, Object>> VARIANCE_RULES() {
        return List.of(
                rulesReader.apply("(Var (? e))", "(Exp (* (+ (: e) (* -1.0 (Exp (: e)))) (+ (: e) (* -1.0 (Exp (: e))))))"));
    }

}
