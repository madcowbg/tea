import matching.Operations;
import matching.simple.SimpleAlgebra;
import matching.simple.SimpleDictionary;
import org.testng.Assert;
import org.testng.annotations.Test;
import matching.simple.GaussianRules;
import matching.simple.Op;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static matching.simple.SimpleAlgebra.Distribution.Normal;

@Test
public class ProbabilisticCalculusTest {

    private final SimpleAlgebra a = new SimpleAlgebra(Op.class);
    private final SimpleAlgebra.Symbol Exp = new SimpleAlgebra.Symbol("Exp");
    private final SimpleAlgebra.Symbol Var = new SimpleAlgebra.Symbol("Var");

    private final GaussianRules<Op, SimpleAlgebra.Symbol> rules = new GaussianRules<>(Op.values(), Exp, Var);

    private final Operations<Object, Object, Object, Object, Object, Object, Object> o = new Operations<>(a, a, a);


    @Test
    public void TestExpecationWithGaussian() {
        var x_1 = new SimpleAlgebra.Symbol("x_1");
        var exp = Op.sum.of(3.0, Op.prod.of(List.of(Normal, x_1, 0.0, 1.0), 4));
        Assert.assertEquals(exp.toString(), "[+, 3.0, [*, [N, x_1, 0.0, 1.0], 4]]");

        var Exp = new SimpleAlgebra.Symbol("Exp");

        var allExpectationRules = Stream.of(
                rules.ALGEBRAIC_SIMPLIFICATION_RULES(),
                rules.DISTRIBUTION_SIMPLIFICATION_RULES(),
                rules.EXPECTATION_RULES(),
                rules.GAUSSIAN_EXPECTATION_RULES()).flatMap(List::stream).collect(Collectors.toList());

        var expectation = o.repeatedlyApplyRules(allExpectationRules, SimpleDictionary::EMPTY, 10000).apply(List.of(Exp, exp));
        Assert.assertTrue(expectation instanceof Number);
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
                rules.DISTRIBUTION_SIMPLIFICATION_RULES(),
                rules.EXPECTATION_RULES(),
                rules.VARIANCE_RULES(),
                rules.GAUSSIAN_EXPECTATION_RULES(),
                rules.ALGEBRAIC_NUMBER_EVALUATION_RULES()
        ).flatMap(List::stream).collect(Collectors.toList());

        var variance = o.repeatedlyApplyRules(allVarianceCalculationRules, SimpleDictionary::EMPTY, 10000).apply(List.of(Var, exp));
        Assert.assertTrue(variance instanceof Number);
        Assert.assertEquals(((Number)variance).doubleValue(), 16.0, 1e-14);
    }

}
