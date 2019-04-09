import matching.Operations;
import matching.simple.SimpleAlgebra;
import matching.simple.SimpleDictionary;
import org.testng.Assert;
import org.testng.annotations.Test;
import matching.simple.GaussianRules;
import matching.simple.Op;
import matching.simple.ProbabilisticRules;
import matching.simple.Rules;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static matching.simple.SimpleAlgebra.Distribution.Normal;

@Test
public class ProbabilisticCalculusTest {

    private final SimpleAlgebra a = new SimpleAlgebra(Op.class);
    private final SimpleAlgebra.Symbol Exp = new SimpleAlgebra.Symbol("Exp");
    private final SimpleAlgebra.Symbol Var = new SimpleAlgebra.Symbol("Var");

    private final Rules<Op> rules = new Rules<>(Op.sum, Op.prod, Op.sign);
    private final ProbabilisticRules<Op, SimpleAlgebra.Symbol> probabilisticRules = new ProbabilisticRules<>(rules, Exp, Var);
    private final GaussianRules<Op, SimpleAlgebra.Symbol> gaussianRules = new GaussianRules<>(probabilisticRules);

    private final Operations<Object, Object, Object, Object, Object, Object, Object> o = new Operations<>(a, a, a);


    @Test
    public void TestExpecationWithGaussian() {
        var x_1 = new SimpleAlgebra.Symbol("x_1");
        var exp = Op.sum.of(3.0, Op.prod.of(List.of(Normal, x_1, 0.0, 1.0), 4));
        Assert.assertEquals(exp.toString(), "[+, 3.0, [*, [N, x_1, 0.0, 1.0], 4]]");

        var Exp = new SimpleAlgebra.Symbol("Exp");

        var allExpectationRules = Stream.of(
                rules.ALGEBRAIC_SIMPLIFICATION_RULES(),
                probabilisticRules.DISTRIBUTION_SIMPLIFICATION_RULES(),
                probabilisticRules.EXPECTATION_RULES(),
                gaussianRules.GAUSSIAN_EXPECTATION_RULES()).flatMap(List::stream).collect(Collectors.toList());

        var expectation = o.repeatedlyApplyRules(allExpectationRules, SimpleDictionary::EMPTY, 10000).apply(List.of(Exp, exp));
        Assert.assertEquals(((Number)expectation).doubleValue(), 3.0, 1e-14);
    }

    @Test
    public void TestProbababilisticCalculusRulesCalculateVariance() {
        var x_1 = new SimpleAlgebra.Symbol("x_1");
        var exp = Op.sum.of(3.0, Op.prod.of(List.of(Normal, x_1, 0.0, 1.0), 4));
        Assert.assertEquals(exp.toString(), "[+, 3.0, [*, [N, x_1, 0.0, 1.0], 4]]");

        var allVarianceCalculationRules = Stream.of(
                rules.ALGEBRAIC_SIMPLIFICATION_RULES(),
                rules.PRODUCT_ALGEBRAIC_SIMPLIFICATION(),
                probabilisticRules.DISTRIBUTION_SIMPLIFICATION_RULES(),
                probabilisticRules.EXPECTATION_RULES(),
                probabilisticRules.VARIANCE_RULES(),
                gaussianRules.GAUSSIAN_EXPECTATION_RULES(),
                rules.ALGEBRAIC_NUMBER_EVALUATION_RULES()
        ).flatMap(List::stream).collect(Collectors.toList());

        var variance = o.repeatedlyApplyRules(allVarianceCalculationRules, SimpleDictionary::EMPTY, 10000).apply(List.of(Var, exp));
        Assert.assertEquals(((Number)variance).doubleValue(), 16.0, 1e-14);
    }

}
