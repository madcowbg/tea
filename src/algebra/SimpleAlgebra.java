package algebra;

import expressions.Op;

public class SimpleAlgebra implements Algebra {

    @Override
    public Expression makeSum(Expression a, Expression b) {
        if (a.isNumber() && ((Number)a).isZero()) {
            return b;
        } else if (b.isNumber() && ((Number)b).isZero()) {
            return a;
        } else if (a.isNumber() && b.isNumber()) {
            return makeNum(((Number)a).value() + ((Number)b).value());
        } else {
            return new SimpleSum(a, b);
        }
    }

    @Override
    public Expression makeProduct(Expression a, Expression b) {
        if ((a.isNumber() && ((Number)a).isZero()) || (b.isNumber() && ((Number)b).isZero())) {
            return makeNum(0);
        } else if (a.isNumber() && ((Number)a).isOne()) {
            return b;
        } else if (b.isNumber() && ((Number)b).isOne()) {
            return a;
        } else if (a.isNumber() && b.isNumber()) {
            return makeNum(((Number)a).value() * ((Number)b).value());
        } else {
            return new SimpleProduct(a, b);
        }
    }

    @Override
    public Expression makeNum(double d) {
        return new SimpleNum(d);
    }
}

class SimpleSum implements Algebra.Sum {
    private final Algebra.Expression a;
    private final Algebra.Expression b;

    public SimpleSum(Algebra.Expression a, Algebra.Expression b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString() {
        return "(" + a.toString() + Op.plus.print() + b.toString() + ")";
    }

    @Override
    public Algebra.Expression addend() {
        return a;
    }

    @Override
    public Algebra.Expression augend() {
        return b;
    }
}

class SimpleProduct implements Algebra.Product {
    private final Algebra.Expression a;
    private final Algebra.Expression b;

    public SimpleProduct(Algebra.Expression a, Algebra.Expression b) {
        this.a = a;
        this.b = b;
    }
    @Override
    public Algebra.Expression multiplier() {
        return a;
    }

    @Override
    public Algebra.Expression multiplicand() {
        return b;
    }

    @Override
    public String toString() {
        return "(" + a.toString() + Op.mul.print() + b.toString() + ")";
    }
}

class SimpleNum implements Algebra.Expression, Algebra.Number {
    private static final double TOL = 1e-12;
    private final double val;

    SimpleNum(double val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    @Override
    public double value() {
        return val;
    }

    @Override
    public boolean isZero() {
        return Math.abs(val) < TOL;
    }

    @Override
    public boolean isOne() {
        return Math.abs(val - 1) < TOL;
    }
}
