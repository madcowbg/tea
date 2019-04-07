import expressions.Op;
import expressions.Term;
import org.testng.Assert;
import org.testng.annotations.Test;

import static expressions.Const.C;
import static expressions.Expr.E;
import static expressions.Gaussian.W;
import static snippets.Main.eval;

@Test()
public class ExpressionsTest {
    private static final double EPS = 1e-13;

    @Test()
    void ConstantExpressionPrintsAndEvaluatesCorrecty() {
        Term t = E(C(6.0), Op.minus, E(E(C(5.0), Op.plus, C(1.0)), Op.mul, C(7.0)));
        Assert.assertEquals(t.print().replace(" ", ""), "(6.0-((5.0+1.0)*7.0))");
        Assert.assertEquals(eval((c) -> c.val, t), (6.0 - ((5.0 + 1.0) * 7.0)), EPS);
    }
    @Test()
    void RandomExpressionPrintsCorrecty() {
        Term t = E(C(6.0), Op.minus, E(E(C(5.0), Op.plus, C(1.0)), Op.mul, E(W(-4.0, 1.0), Op.plus, C(7.0))));
        Assert.assertEquals(t.print().replace(" ", ""), "(6.0-((5.0+1.0)*(N(-4.0,1.0)+7.0)))");
    }
}