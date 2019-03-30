package snippets;

import algebra.Algebra;
import algebra.SimpleAlgebra;
import algebra.SimpleVariable;
import expressions.Expr;
import functional.Maybe;

import java.util.function.Function;

import static algebra.Operations.deriv;

public class MainAlgebra {

    public static void main(String[] args) {
        Algebra a = new SimpleAlgebra();
        Algebra.Variable v = new SimpleVariable("x");
        Algebra.Variable y = new SimpleVariable("y");
        Algebra.Expression e = a.makeSum(a.makeProduct(v, a.makeProduct(y, v)), a.makeSum(y, v));
        System.out.println(e.toString());

        Function<Algebra.Expression, Maybe<Algebra.Expression>> dd = (e2) -> deriv(a).apply(e2, v);
        System.out.println(deriv(a).apply(e, v).toString());
        System.out.println(Maybe.repeat(dd, 2).apply(e).toString());
        System.out.println(Maybe.repeat(dd, 3).apply(e).toString());
    }
}
