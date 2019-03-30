package snippets;

import expressions.*;

import java.util.function.Function;

import static expressions.Const.C;
import static expressions.Expr.E;


public class Main {
//    interface EvalFun<T> extends Function<Term, T> {}
//
//    static Map<Class, EvalFun<Double>> toDouble = new HashMap<>();
//
//    static {
//        toDouble.put(Expr.class, Main.evalExpr());
//    }
//
//    static <T> EvalFun<T> evalExpr(Map<Class, EvalFun<T>> mapper) {
//        return (Term s) -> {
//            Expr e = (Expr) s;
//            switch (e.op) {
//                case plus:
//                    return eval(mapper).apply(e.a) + eval(mapper).apply(e.b);
//                case minus:
//                    return eval(mapper).apply(e.a) + eval(mapper).apply(e.b);
//                case times:
//                    return eval(mapper).apply(e.a) * eval(mapper).apply(e.b);
//                default:
//                    throw new UnsupportedOperationException();
//            }
//        }
//    }
//
//    static <T> Function<Term, T> eval(Map<Class, EvalFun<T>> mapper) {
//        return (term) -> mapper.get(term.getClass()).apply(term);
//    }

    public static double eval(Function<Const, Double> f, Term t) {
        if (t instanceof Expr) {
            Expr e = (Expr) t;
            return e.op.apply(eval(f, e.a), eval(f, e.b));
        } else if (t instanceof Const) {
            return f.apply((Const)t);
        } else {
            throw new UnsupportedOperationException(t.getClass().getName());
        }
    }

//
//    static <T> Function<Term, Maybe<T>> genericApply(Map<Class, Function<Object, T>> map) {
//        return t ->
//                Optional.ofNullable(map.get(t.getClass()))
//                        .map(f -> f.apply(t))
//                        .map(Maybe::of)
//                        .orElse(Maybe.fail("unsupported class " + t.getClass()));
//    }

    static double mean(Term t) {
        if (t instanceof Expr) {
            Expr e = (Expr) t;
            return e.op.apply(mean(e.a), mean(e.b));
        } else if (t instanceof Const) {
            return ((Const)t).val;
        } else if (t instanceof Gaussian) {
            return ((Gaussian)t).mu;
        } else {
            throw new UnsupportedOperationException(t.getClass().getName());
        }
    }

//    static double variance(Term t) {
//        if (t instanceof Expr) {
//            Expr e = (Expr) t;
//            return e.op.apply(mean(e.a), mean(e.b));
//        } else if (t instanceof Const) {
//            return ((Const)t).val;
//        } else if (t instanceof Gaussian ) {
//            return ((Gaussian)t).mu;
//        } else {
//            throw new UnsupportedOperationException(t.getClass().getName());
//        }
//    }

    public static void main(String[] args) {
        Term t = E(C(6.0), Op.minus, E(E(C(5.0), Op.plus, C(1.0)), Op.times, C(7.0)));
        System.out.println(t.print());
        System.out.println(mean(t));
        System.out.println(eval((c) -> c.val, t));

        t = E(C(6.0), Op.minus, E(E(C(5.0), Op.plus, C(1.0)), Op.times, E(Gaussian.W(-4.0, 1.0), Op.plus, C(7.0))));
        System.out.println(t.print());
        System.out.println(mean(t));
        // System.out.println(eval((c) -> c.val, t));
    	// write your code here
    }

}
