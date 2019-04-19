package snippets;

import matching.*;
import matching.simple.*;
import sexpressions.Reader;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static matching.simple.SimpleAlgebra.Distribution.Normal;

public class MainSimplifier {
    private static final SimpleAlgebra a = new SimpleAlgebra(Op.class);
    private static final Operations<Object, Object, Object, Object, Object, Object, Object> o = new Operations<>(a, a, a);

    private static final SimpleAlgebra.Symbol Exp = new SimpleAlgebra.Symbol("Exp");
    private static final SimpleAlgebra.Symbol Var = new SimpleAlgebra.Symbol("Var");

    private static Rules<Op> rules = new Rules<>(Op.values());
    private static ProbabilisticRules<Op, SimpleAlgebra.Symbol> probabilisticRules = new ProbabilisticRules<>(Op.values(), Exp, Var);
    private static GaussianRules<Op, SimpleAlgebra.Symbol> gaussianRules = new GaussianRules<>(Op.values(), Exp, Var);

    public static void main(String[] args) {
//        trySimpleProb();
//        tryComplexProb();
//        tryExpectations();
        loadData();
    }

    private static void loadData() {

        var exp = Reader.STRING.readSExp("(+ 2 (* 3.0 (N x_1 1 1)))");
        System.out.println(exp);

        Function<String, Op> ops = Arrays.stream(Op.values()).collect(Collectors.toMap(Op::toString, Function.identity()))::get;
        Function<String, SimpleAlgebra.Distribution> dists = (dname) -> dname.equals("N") ? Normal : null;
        Function<String, SimpleAlgebra.Symbol> symbols = SimpleAlgebra.Symbol::new;

        var pat = RulesReader.convertExpression(RulesReader.convertToken(List.of(ops, dists, symbols))).apply(exp.orElseThrow(RuntimeException::new));

        System.out.println(pat);
    }

    /*
    private static void tryExpectations() {
        var x_1 = new SimpleAlgebra.Symbol("x_1");
        var exp = List.of(Op.plus, 3.0, List.of(Op.mul, List.of(Normal, x_1, 0.0, 1.0), 4));
        System.out.println(exp);


        var allExpectationRules = Stream.of(
                rules.ALGEBRAIC_SIMPLIFICATION_RULES(),
                probabilisticRules.DISTRIBUTION_SIMPLIFICATION_RULES(),
                probabilisticRules.EXPECTATION_RULES(),
                gaussianRules.GAUSSIAN_EXPECTATION_RULES()).flatMap(List::stream).collect(Collectors.toList());

        var expectation = o.repeatedlyApplyRules(allExpectationRules, SimpleDictionary::EMPTY, 10000).apply(List.of(Exp, exp));
        System.out.println(expectation);


        var allVarianceRules = Stream.of(
                rules.ALGEBRAIC_SIMPLIFICATION_RULES(),
                rules.PRODUCT_ALGEBRAIC_SIMPLIFICATION(),
                probabilisticRules.DISTRIBUTION_SIMPLIFICATION_RULES(),
                probabilisticRules.EXPECTATION_RULES(),
                probabilisticRules.VARIANCE_RULES(),
                gaussianRules.GAUSSIAN_EXPECTATION_RULES(),
                rules.ALGEBRAIC_NUMBER_EVALUATION_RULES()
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
    }*/

}
