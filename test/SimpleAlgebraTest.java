import algebra.Algebra;
import algebra.SimpleAlgebra;
import algebra.SimpleVariable;
import functional.Maybe;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.function.Function;

import static algebra.Operations.deriv;

@Test
public class SimpleAlgebraTest {
    @Test
    void TestDerivativesStringRepresentation() {
        Algebra a = new SimpleAlgebra();
        Algebra.Variable v = new SimpleVariable("x");
        Algebra.Variable y = new SimpleVariable("y");
        Algebra.Expression e = a.makeSum(a.makeProduct(v, a.makeProduct(y, v)), a.makeSum(y, v));
        Assert.assertEquals(e.toString().replace(" ", ""), "((x*(y*x))+(y+x))");

        Function<Algebra.Expression, Maybe<Algebra.Expression>> dd_v = (e2) -> deriv(a).apply(e2, v);
        Assert.assertEquals(
                deriv(a).apply(e, v).toString().replace(" ", ""),
                "ok[(((y*x)+(x*y))+1.0)]");
        Assert.assertEquals(
                Maybe.repeat(dd_v, 2).apply(e).toString().replace(" ", ""),
                "ok[(y+y)]");
        Assert.assertEquals(
                Maybe.repeat(dd_v, 3).apply(e).toString().replace(" ", ""),
                "ok[0.0]");
    }
}
