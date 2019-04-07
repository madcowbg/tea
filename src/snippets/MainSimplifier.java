package snippets;

import expressions.Op;
import functional.Maybe;
import matching.*;
import matching.simple.*;

import java.util.List;

import static matching.simple.SimpleAlgebra.Distribution.Normal;
import static snippets.Probabilistic.PROBABILISTIC_NORMAL_SIMPLIFIER_RULES;

public class MainSimplifier {


    public static void main(String[] args) {
//        trySimpleProb();
        tryComplexProb();
    }

    static void tryComplexProb() {
        var a = new SimpleAlgebra();
        var o = new Operations<>(a, a, a);

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

        var ultraSimplified = o.repeatedlyApplyRules(PROBABILISTIC_NORMAL_SIMPLIFIER_RULES, SimpleDictionary::EMPTY, 10000).apply(exp);
        System.out.println(ultraSimplified);

        var other = List.of(Op.plus, List.of(Op.mul, List.of(Normal, x_1, 0.0, 1.0), 3.0), 3.0);
        System.out.println(o.repeatedlyApplyRules(PROBABILISTIC_NORMAL_SIMPLIFIER_RULES, SimpleDictionary::EMPTY, 10000).apply(other));

    }

    static void trySimpleProb() {
        var a = new SimpleAlgebra();
        var o = new Operations<>(a, a, a);

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
        var a = new SimpleAlgebra();
        var exp = List.of(Op.plus, 5.0, 7.0);

        var patArbExp = List.of("?", "x");
        var pat = List.of(Op.plus, patArbExp, 7.0);

        System.out.println(exp);
        System.out.println(pat);

        var emptyDict = SimpleDictionary.EMPTY();

        var o = new Operations<>(a, a, a);
        var dict = o.match(pat, exp, emptyDict);
        System.out.println(dict);

        var s = List.of(Op.div, 3.0, List.of(":", "x"));
        System.out.println(s);

        var instantiated = dict.map(d -> o.instantiate(d, s));
        System.out.println(instantiated);
    }

}
