import expressions.Op;
import functional.Maybe;
import matching.Dictionary;
import matching.MatchedVariable;
import matching.Operations;
import matching.simple.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;


@Test
public class SimpleMatchingTest {
    @Test
    void TestMatchInstantiate() {
        var a = new SimpleAlgebra();
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
        Assert.assertEquals(instantiated.toString(), "ok[[/, [3.0, 5.0]]]");
    }
}
