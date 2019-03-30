package expressions;

public class Expr implements Term {
    public final Op op;
    public final Term a;
    public final Term b;

    protected Expr(Term a, Op op, Term b) {
        this.op = op;
        this.a = a;
        this.b = b;
    }

    public String print() {
        return "(" + a.print() + " " + op.print() + " " + b.print() + ")";
    }

    public static Expr E(Term a, Op op, Term b) {
        return new Expr(a, op, b);
    }
}
