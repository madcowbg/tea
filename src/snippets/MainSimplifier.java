package snippets;

import expressions.Op;
import functional.Maybe;
import matching.*;
import matching.simple.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static matching.simple.SimpleAlgebra.Distribution.Normal;
import static snippets.GaussianRules.GAUSSIAN_EXPECTATION_RULES;
import static snippets.ProbabilisticRules.*;
import static snippets.Rules.*;

public class MainSimplifier {
    private static final SimpleAlgebra a = new SimpleAlgebra();
    private static final Operations<Object, Object, Object, Object, Object, Object, Object> o = new Operations<>(a, a, a);

    public static void main(String[] args) {
//        trySimpleProb();
//        tryComplexProb();
        tryExpectations();
    }

    private static void tryExpectations() {
        var x_1 = new SimpleAlgebra.Symbol("x_1");
        var exp = List.of(Op.plus, 3.0, List.of(Op.mul, List.of(Normal, x_1, 0.0, 1.0), 4));
        System.out.println(exp);

        var Exp = new SimpleAlgebra.Symbol("Exp");

        var allExpectationRules = Stream.of(
                ALGEBRAIC_SIMPLIFICATION_RULES,
                DISTRIBUTION_SIMPLIFICATION_RULES,
                EXPECTATION_RULES(Exp),
                GAUSSIAN_EXPECTATION_RULES(Exp)).flatMap(List::stream).collect(Collectors.toList());

        var expectation = o.repeatedlyApplyRules(allExpectationRules, SimpleDictionary::EMPTY, 10000).apply(List.of(Exp, exp));
        System.out.println(expectation);

        var Var = new SimpleAlgebra.Symbol("Var");

        var allVarianceRules = Stream.of(
                ALGEBRAIC_SIMPLIFICATION_RULES,
                PRODUCT_ALGEBRAIC_SIMPLIFICATION,
                DISTRIBUTION_SIMPLIFICATION_RULES,
                EXPECTATION_RULES(Exp),
                VARIANCE_RULES(Exp, Var),
                //GAUSSIAN_DISTRIBUTION_INCORPORATE_LINEAR_COMBINATION_RULES,
                GAUSSIAN_EXPECTATION_RULES(Exp),
                ALGEBRAIC_NUMBER_EVALUATION_RULES
                ).flatMap(List::stream).collect(Collectors.toList());

        var variance = o.repeatedlyApplyRules(allVarianceRules, SimpleDictionary::EMPTY, 10000).apply(List.of(Var, exp));
        System.out.println(variance);
    }

    static void tryComplexProb() {

        var x_1 = new SimpleAlgebra.Symbol("x_1");
        var exp = List.of(Op.plus, 3.0, List.of(Op.mul, 3.0, List.of(Normal, x_1, 0.0, 1.0)));
        System.out.println(exp);

        var pat = List.of(Op.mul, List.of("?c", "m"), List.of(Normal, List.of("?", "v"), List.of("?", "mu"), List.of("?", "sigma")));
        System.out.println(pat);

        var skeleton = List.of(Normal, List.of(":", "v"), List.of(Op.mul, List.of(":", "m"), List.of(":", "mu")), List.of(Op.mul, List.of(":", "m"), List.of(":", "sigma")));
        System.out.println(skeleton);

        var rule = new Operations.Rule<Object, Object>(pat, skeleton);
        var simplified = o.applyRecursively(rule, SimpleDictionary::EMPTY).apply(exp);
        System.out.println(simplified);

        var simplified2 = o.applyRecursively(rule, SimpleDictionary::EMPTY).apply(simplified);
        System.out.println(simplified2);

        System.out.println(a.isExpressionEqual(simplified, simplified2));

        Collection<Operations.Rule<Object, Object>> rules = List.of(
                new Operations.Rule<>(
                        List.of(Op.mul, List.of("?c", "m"), List.of(Normal, List.of("?", "v"), List.of("?", "mu"), List.of("?", "sigma"))),
                        List.of(Normal, List.of(":", "v"), List.of(Op.mul, List.of(":", "m"), List.of(":", "mu")), List.of(Op.mul, List.of(":", "m"), List.of(":", "sigma")))),
                new Operations.Rule<>(
                        List.of(Op.plus, List.of("?c", "a"), List.of(Normal, List.of("?", "v"), List.of("?", "mu"), List.of("?", "sigma"))),
                        List.of(Normal, List.of(":", "v"), List.of(Op.plus, List.of(":", "a"), List.of(":", "mu")), List.of(":", "sigma"))),
                new Operations.Rule<>(
                        List.of(Op.mul, List.of("?d", "dist"), List.of("?c", "m")),
                        List.of(Op.mul, List.of(":", "m"), List.of(":", "dist"))),
                new Operations.Rule<>(
                        List.of(Op.plus, List.of("?d", "dist"), List.of("?c", "a")),
                        List.of(Op.plus, List.of(":", "a"), List.of(":", "dist"))),
                new Operations.Rule<>(List.of(Op.mul, List.of("?", "x"), 1.0d), List.of(":", "x")),
                new Operations.Rule<>(List.of(Op.mul, 1.0d, List.of("?", "x")), List.of(":", "x")),
                new Operations.Rule<>(List.of(Op.mul, List.of("?", "x"), 0.0d), 0.0d),
                new Operations.Rule<>(List.of(Op.mul, 0.0d, List.of("?", "x")), 0.0d),
                new Operations.Rule<>(List.of(Op.plus, List.of("?", "x"), 0.0d), List.of(":", "x")),
                new Operations.Rule<>(List.of(Op.plus, 0.0d, List.of("?", "x")), List.of(":", "x")));
        var ultraSimplified = o.repeatedlyApplyRules(rules, SimpleDictionary::EMPTY, 10000).apply(exp);
        System.out.println(ultraSimplified);

        var other = List.of(Op.plus, List.of(Op.mul, List.of(Normal, x_1, 0.0, 1.0), 3.0), 3.0);
        System.out.println(o.repeatedlyApplyRules(rules, SimpleDictionary::EMPTY, 10000).apply(other));

        var zeroExp = List.of(Op.mul, List.of(Op.mul, List.of(Normal, x_1, 0, 1), 3), 0);
        System.out.println(o.repeatedlyApplyRules(rules, SimpleDictionary::EMPTY, 10000).apply(zeroExp));
    }

    static void trySimpleProb() {
        var x_1 = new SimpleAlgebra.Symbol("x_1");
        var exp = List.of(Op.mul, 3.0, List.of(Normal, x_1, 0.0, 1.0));
        System.out.println(exp);

        var pat = List.of(Op.mul, List.of("?c", "m"), List.of(Normal, List.of("?", "v"), List.of("?", "mu"), List.of("?", "sigma")));
        System.out.println(pat);

        var dict = o.match(pat, exp, SimpleDictionary.EMPTY());
        System.out.println(dict);

        var s = List.of(Normal, List.of(":", "v"), List.of(Op.mul, List.of(":", "m"), List.of(":", "mu")), List.of(Op.mul, List.of(":", "m"), List.of(":", "sigma")));
        System.out.println(s);
        var instantiated = Maybe.lift(o::instantiate).apply(dict, Maybe.ok(s));
        System.out.println(instantiated);
    }

    private static void runAlgSim() {
        var exp = List.of(Op.plus, 5.0, 7.0);

        var patArbExp = List.of("?", "x");
        var pat = List.of(Op.plus, patArbExp, 7.0);

        System.out.println(exp);
        System.out.println(pat);

        var emptyDict = SimpleDictionary.EMPTY();

        var dict = o.match(pat, exp, emptyDict);
        System.out.println(dict);

        var s = List.of(Op.div, 3.0, List.of(":", "x"));
        System.out.println(s);

        var instantiated = dict.map(d -> o.instantiate(d, s));
        System.out.println(instantiated);
    }

}
