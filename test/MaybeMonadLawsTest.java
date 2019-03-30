import functional.Maybe;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.function.Function;

import static functional.Maybe.fail;
import static functional.Maybe.ok;

public class MaybeMonadLawsTest {
    private Function<Integer, Maybe<Integer>> ok_ = Maybe::ok;
    private Function<Integer, Maybe<Integer>> fail_ = val -> fail("why not...");

    private Function<Integer, Maybe<Integer>> f = val -> ok(val + 1);
    private Function<Integer, Maybe<Integer>> g = val -> ok(val + 3);

    @Test(description = "unit(a) flatMap f === f(a)")
    public void LeftIdentity() {
        int val = 2;
        Assert.assertEquals(ok(val).bind(f), f.apply(val));

        Assert.assertEquals(Maybe.<Integer>fail("why not...").bind(f), fail_.apply(val));
    }

    @Test(description = "m flatMap unit === m")
    public void RightIdentity() {
        Maybe<Integer> result = ok(2);
        Assert.assertEquals(result.bind(ok_), result);

        var failResult = fail("why not...");
        Assert.assertEquals(result.bind(fail_), failResult);
    }

    @Test(description = "(m flatMap f) flatMap g === m flatMap ( f(x) flatMap g )")
    public void Associativity() {
        Maybe<Integer> result = ok(2);
        Maybe<Integer> left = (result.bind(f)).bind(g);
        Maybe<Integer> right = result.bind(val -> f.apply(val).bind(g));

        Assert.assertEquals(left, right);

        Maybe<Integer> failResult = fail("why not...");
        Maybe<Integer> leftFail = (failResult.bind(f)).bind(g);
        Maybe<Integer> rightFail = failResult.bind(val -> f.apply(val).bind(g));

        Assert.assertEquals(leftFail, rightFail);
    }

    @Test
    public void LiftingOperatorCombinesCorrectly() {
        var lifted = Maybe.lift((Double a, Integer b) -> (float) (a + b));
        Assert.assertEquals(lifted.apply(ok(2.0), ok(3)), ok(5.0f));
        Assert.assertEquals(lifted.apply(ok(2.0), fail("")), fail(""));
        Assert.assertEquals(lifted.apply(fail(""), ok(3)), fail(""));
    }

    @Test
    public void OrElseReturnsOrConsumesWhenNeeded() {
        var res = ok(5.0);
        Assert.assertEquals(res.orElse(7.0), 5.0);
        Assert.assertEquals(res.orElse((f) -> {
            throw new RuntimeException("Should not be called!");
        }), 5.0);

        var failed = fail("yeah...");
        Assert.assertEquals(failed.orElse(8.0), 8.0);
        Assert.assertEquals(failed.orElse((f) -> {
            if (!f.equals(failed)) {
                throw new RuntimeException("are not equal, should be!");
            }
            return 9.0;
        }), 9.0);
    }
}