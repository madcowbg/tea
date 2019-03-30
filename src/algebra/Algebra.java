package algebra;

public interface Algebra {
    interface Expression {
        default boolean isNumber() {
            return this instanceof Number;
        }
        default boolean isVariable() {
            return this instanceof Variable;
        }
        default boolean isSum() {
            return this instanceof Sum;
        }
        default boolean isProduct() {
            return this instanceof Product;
        }
    }

    interface Number extends Expression {
        double value();
        boolean isZero();
        boolean isOne();
    }

    interface Variable extends Expression {
        boolean areSameVariableAs(Variable other);
    }

    interface Sum extends Expression  {
        Expression addend();
        Expression augend();
    }

    interface Product extends Expression {
        Expression multiplier();
        Expression multiplicand();
    }

    Expression makeSum(Expression a, Expression b);
    Expression makeProduct(Expression a, Expression b);

    Expression makeNum(double d);
}
