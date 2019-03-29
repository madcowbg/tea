import expressions.Const;
import expressions.Expr;
import expressions.Op;
import expressions.Term;

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

    static double calc(Term t) {
        if (t instanceof Expr) {
            Expr e = (Expr) t;
            return e.op.apply(calc(e.a), calc(e.b));
        } else if (t instanceof Const) {
            return ((Const)t).val;
        } else {
            throw new UnsupportedOperationException(t.getClass().getName());
        }
    }




    public static void main(String[] args) {
        Term t = E(C(6.0), Op.minus, E(E(C(5.0), Op.plus, C(1.0)), Op.times, C(7.0)));

        System.out.println(t.print());
        System.out.println(calc(t));

    	// write your code here
    }
}
