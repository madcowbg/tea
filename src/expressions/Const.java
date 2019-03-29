package expressions;

public class Const implements Term {
    public final double val;

    private Const(double val) {
        this.val = val;
    }

    public static Term C(double c) {
        return new Const(c);
    }

    @Override
    public String print() {
        return Double.toString(val);
    }
}
