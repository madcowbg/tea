import expressions.Op;
import matching.MatchedVariable;
import matching.simple.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import static matching.Operations.instantiate;
import static matching.Operations.match;

@Test
public class SimpleMatchingTest {
    @Test
    void TestMatchInstantiate() {
        var n5 = new SimpleAlgebra.Number(5);
        var n7 = new SimpleAlgebra.Number(7);
        var exp = new SimpleAlgebra.TripletExpression(Op.plus, n5, n7);

        MatchedVariable x = new SimpleMatchedVariable("x");
        var patArbExp = new SimplePattern.SimpleArbitraryExpression(x);
        var pat = new SimplePattern.TripletPattern(Op.plus, patArbExp, n7);

        Assert.assertEquals(exp.toString(), "(+ 5.0 7.0)");
        Assert.assertEquals(pat.toString(), "(+ ((? x) 7.0))");

        var emptyDict = SimpleDictionary.EMPTY;

        var dict = match(pat, exp, emptyDict);
        Assert.assertEquals(dict.toString(), "ok[x -> 5.0]");

        var n3 = new SimpleAlgebra.Number(3);
        var sv = new SimpleSkeleton.SimpleSkeletonEvaluation(x);
        var s = new SimpleSkeleton.TripletSkeleton(Op.div, n3, sv);
        Assert.assertEquals(s.toString(), "(/ (3.0 (: x)))");

        var instantiated = dict.map(d -> instantiate(d, s));
        Assert.assertEquals(instantiated.toString(), "ok[(/ 3.0 5.0)]");
    }
}
