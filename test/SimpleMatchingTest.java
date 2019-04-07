import expressions.Op;
import functional.Maybe;
import matching.Dictionary;
import matching.MatchedVariable;
import matching.Operations;
import matching.simple.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static matching.simple.SimpleAlgebra.Distribution.Normal;
import static snippets.Probabilistic.PROBABILISTIC_NORMAL_SIMPLIFIER_RULES;


@Test
public class SimpleMatchingTest {
    private final SimpleAlgebra a = new SimpleAlgebra();
    private final Operations<Object, Object, Object, Object, Object, Object, Object> o = new Operations<>(a, a, a);

    @Test
    void TestMatchInstantiate() {
        Double n5 = 5.0;
        Double n7 = 7.0;
        var exp = List.of(Op.plus, n5, n7);

        var patArbExp = List.of("?", "x");
        var pat = List.of(Op.plus, patArbExp, n7);

        Assert.assertEquals(exp.toString(), "[+, 5.0, 7.0]");
        Assert.assertEquals(pat.toString(), "[+, [?, x], 7.0]");

        var emptyDict = SimpleDictionary.EMPTY();

        var o = new Operations<>(a, a, a);
        Maybe<Dictionary<Object>> dict = o.match(pat, exp, emptyDict);
        Assert.assertEquals(dict.toString(), "ok[x -> 5.0]");

        var n3 = 3.0;
        var s = List.of(Op.div, n3, List.of(":", "x"));
        Assert.assertEquals(s.toString(), "[/, 3.0, [:, x]]");

        var instantiated = dict.map(d -> o.instantiate(d, s));
        Assert.assertEquals(instantiated.toString(), "ok[[/, 3.0, 5.0]]");
    }

    @Test
    void TestProbabilisticRuleApplicationInstantiatesCorrectly() {
        var x_1 = new SimpleAlgebra.Symbol("x_1");
        var exp = List.of(Op.mul, 3.0, List.of(Normal, x_1, 0.0, 1.0));
        Assert.assertEquals(exp.toString(), "[*, 3.0, [N, x_1, 0.0, 1.0]]");

        var pat = List.of(Op.mul, List.of("?c", "m"), List.of(Normal, List.of("?", "v"), List.of("?", "mu"), List.of("?", "sigma")));
        Assert.assertEquals(pat.toString(), "[*, [?c, m], [N, [?, v], [?, mu], [?, sigma]]]");

        var dict = o.match(pat, exp, SimpleDictionary.EMPTY());
        Assert.assertEquals(dict.toString(), "ok[mu -> 0.0\n" + "sigma -> 1.0\n" + "m -> 3.0\n" + "v -> x_1]");

        var s = List.of(Normal, List.of(":", "v"), List.of(Op.mul, List.of(":", "m"), List.of(":", "mu")), List.of(Op.mul, List.of(":", "m"), List.of(":", "sigma")));
        Assert.assertEquals(s.toString(), "[N, [:, v], [*, [:, m], [:, mu]], [*, [:, m], [:, sigma]]]");

        var instantiated = Maybe.lift(o::instantiate).apply(dict, Maybe.ok(s));
        Assert.assertEquals(instantiated.toString(), "ok[[N, x_1, [*, 3.0, 0.0], [*, 3.0, 1.0]]]");
    }

    @Test
    void TestRecursiveRuleSimplifiesCorrectly() {
        var x_1 = new SimpleAlgebra.Symbol("x_1");
        var exp = List.of(Op.plus, 3.0, List.of(Op.mul, 3.0, List.of(Normal, x_1, 0.0, 1.0)));
        Assert.assertEquals(exp.toString(), "[+, 3.0, [*, 3.0, [N, x_1, 0.0, 1.0]]]");

        var pat = List.of(Op.mul, List.of("?c", "m"), List.of(Normal, List.of("?", "v"), List.of("?", "mu"), List.of("?", "sigma")));
        Assert.assertEquals(pat.toString(), "[*, [?c, m], [N, [?, v], [?, mu], [?, sigma]]]");

        var skeleton = List.of(Normal, List.of(":", "v"), List.of(Op.mul, List.of(":", "m"), List.of(":", "mu")), List.of(Op.mul, List.of(":", "m"), List.of(":", "sigma")));
        Assert.assertEquals(skeleton.toString(), "[N, [:, v], [*, [:, m], [:, mu]], [*, [:, m], [:, sigma]]]");

        var rule = new Operations.Rule<Object, Object>(pat, skeleton);
        var simplified = o.applyRecursively(rule, SimpleDictionary::EMPTY).apply(exp);
        Assert.assertEquals(simplified.toString(), "[+, 3.0, [N, x_1, [*, 3.0, 0.0], [*, 3.0, 1.0]]]");

        var simplified2 = o.applyRecursively(rule, SimpleDictionary::EMPTY).apply(simplified);
        Assert.assertEquals(simplified2.toString(), "[+, 3.0, [N, x_1, [*, 3.0, 0.0], [*, 3.0, 1.0]]]");

        Assert.assertTrue(a.isExpressionEqual(simplified, simplified2));
    }

    @Test
    void TestRepeatedRecursiveRuleSimplifiesCorrectly() {
        var x_1 = new SimpleAlgebra.Symbol("x_1");

        var exp = List.of(Op.plus, 3.0, List.of(Op.mul, 3.0, List.of(Normal, x_1, 0.0, 1.0)));
        Assert.assertEquals(exp.toString(), "[+, 3.0, [*, 3.0, [N, x_1, 0.0, 1.0]]]");

        var ultraSimplified = o.repeatedlyApplyRules(PROBABILISTIC_NORMAL_SIMPLIFIER_RULES, SimpleDictionary::EMPTY, 10000).apply(exp);
        Assert.assertEquals(ultraSimplified.toString(), "[N, x_1, 3.0, 3.0]");

        var other = List.of(Op.plus, List.of(Op.mul, List.of(Normal, x_1, 0.0, 1.0), 3.0), 3.0);
        Assert.assertEquals(other.toString(), "[+, [*, [N, x_1, 0.0, 1.0], 3.0], 3.0]");

        var ultraSimplified2 = o.repeatedlyApplyRules(PROBABILISTIC_NORMAL_SIMPLIFIER_RULES, SimpleDictionary::EMPTY, 10000).apply(other);
        Assert.assertEquals(ultraSimplified2.toString(), "[N, x_1, 3.0, 3.0]");

        Assert.assertTrue(a.isExpressionEqual(ultraSimplified, ultraSimplified2));
    }
}
