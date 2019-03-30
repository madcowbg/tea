package algebra;

import functional.Maybe;

import java.util.function.BiFunction;
import java.util.function.Function;

import static functional.Maybe.fail;
import static functional.Maybe.ok;

public interface Operations {

    static BiFunction<Algebra.Expression, Algebra.Variable, Maybe<Algebra.Expression>> deriv(Algebra a) {
        return (Algebra.Expression e, Algebra.Variable v) -> {
            var sum = Maybe.lift(a::makeSum);
            var prod = Maybe.lift(a::makeProduct);
            Function<Algebra.Expression, Maybe<Algebra.Expression>> dd = (expr) -> deriv(a).apply(expr, v);
            if (e.isNumber()) {
                return ok(a.makeNum(0));
            } else if (e.isVariable()) {
                return ok(v.areSameVariableAs((Algebra.Variable) e) ? a.makeNum(1) : a.makeNum(0));
            } else if (e.isSum()) {
                Algebra.Sum s = (Algebra.Sum)e;
                return sum.apply(dd.apply(s.addend()), dd.apply(s.augend()));
            } else if (e.isProduct()) {
                Algebra.Product p = (Algebra.Product) e;
                return sum.apply(
                        prod.apply(dd.apply(p.multiplier()), ok(p.multiplicand())),
                        prod.apply(ok(p.multiplier()), dd.apply(p.multiplicand())));
            } else {
                return fail("unknown expression type -- DERIV " + e);
            }
        };
    }
}
