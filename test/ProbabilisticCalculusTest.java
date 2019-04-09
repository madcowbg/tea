import expressions.Op;
import matching.Operations;
import matching.simple.SimpleAlgebra;
import matching.simple.SimpleDictionary;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static matching.simple.SimpleAlgebra.Distribution.Normal;
import static snippets.GaussianRules.GAUSSIAN_EXPECTATION_RULES;
import static snippets.ProbabilisticRules.*;
import static snippets.Rules.*;

@Test
public class ProbabilisticCalculusTest {
    private final SimpleAlgebra a = new SimpleAlgebra(Op.class);
    private final Operations<Object, Object, Object, Object, Object, Object, Object> o = new Operations<>(a, a, a);

    private final SimpleAlgebra.Symbol Exp = new SimpleAlgebra.Symbol("Exp");
    private final SimpleAlgebra.Symbol Var = new SimpleAlgebra.Symbol("Var");

    @Test
    public void TestExpecationWithGaussian() {
        var x_1 = new SimpleAlgebra.Symbol("x_1");
        var exp = List.of(Op.plus, 3.0, List.of(Op.mul, List.of(Normal, x_1, 0.0, 1.0), 4));
        Assert.assertEquals(exp.toString(), "[+, 3.0, [*, [N, x_1, 0.0, 1.0], 4]]");

        var Exp = new SimpleAlgebra.Symbol("Exp");

        var allExpectationRules = Stream.of(
                ALGEBRAIC_SIMPLIFICATION_RULES,
                DISTRIBUTION_SIMPLIFICATION_RULES,
                EXPECTATION_RULES(Exp),
                GAUSSIAN_EXPECTATION_RULES(Exp)).flatMap(List::stream).collect(Collectors.toList());

        var expectation = o.repeatedlyApplyRules(allExpectationRules, SimpleDictionary::EMPTY, 10000).apply(List.of(Exp, exp));
        Assert.assertEquals(((Number)expectation).doubleValue(), 3.0, 1e-14);
    }

    @Test
    public void TestProbababilisticCalculusRulesCalculateVariance() {
        var x_1 = new SimpleAlgebra.Symbol("x_1");
        var exp = List.of(Op.plus, 3.0, List.of(Op.mul, List.of(Normal, x_1, 0.0, 1.0), 4));
        Assert.assertEquals(exp.toString(), "[+, 3.0, [*, [N, x_1, 0.0, 1.0], 4]]");

        var allVarianceCalculationRules = Stream.of(
                ALGEBRAIC_SIMPLIFICATION_RULES,
                PRODUCT_ALGEBRAIC_SIMPLIFICATION,
                DISTRIBUTION_SIMPLIFICATION_RULES,
                EXPECTATION_RULES(Exp),
                VARIANCE_RULES(Exp, Var),
                GAUSSIAN_EXPECTATION_RULES(Exp),
                ALGEBRAIC_NUMBER_EVALUATION_RULES
        ).flatMap(List::stream).collect(Collectors.toList());

        var variance = o.repeatedlyApplyRules(allVarianceCalculationRules, SimpleDictionary::EMPTY, 10000).apply(List.of(Var, exp));
        Assert.assertEquals(((Number)variance).doubleValue(), 16.0, 1e-14);
    }

}
