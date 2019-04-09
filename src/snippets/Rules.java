package snippets;

import matching.Operations;
import matching.Operator;

import java.util.List;

;
public class Rules<Op extends Operator> {

    protected final Op sum;
    protected final Op prod;
    protected final Op sign;

    public Rules(Op sum, Op prod, Op sign) {
        this.sum = sum;
        this.prod = prod;
        this.sign = sign;
    }

    public final List<Operations.Rule<Object, Object>> ALGEBRAIC_NUMBER_EVALUATION_RULES() {
        return List.of(
                new Operations.Rule<>( // eval sign(b)
                        sign.of(List.of("?c", "a")),
                        List.of(":eval", sign.of(List.of(":", "a")))),
                new Operations.Rule<>( // eval a + b
                        sum.of(List.of("?c", "a"), List.of("?c", "b")),
                        List.of(":eval", sum.of(List.of(":", "a"), List.of(":", "b")))),
                new Operations.Rule<>( // eval a * b
                        prod.of(List.of("?c", "a"), List.of("?c", "b")),
                        List.of(":eval", prod.of(List.of(":", "a"), List.of(":", "b")))));
    }

    public final List<Operations.Rule<Object, Object>> ALGEBRAIC_SIMPLIFICATION_RULES() {
            return List.of(
                new Operations.Rule<>(List.of(prod, List.of("?", "x"), 1.0d), List.of(":", "x")),
                new Operations.Rule<>(List.of(prod, 1.0d, List.of("?", "x")), List.of(":", "x")),
                new Operations.Rule<>(List.of(prod, List.of("?", "x"), 0.0d), 0.0d),
                new Operations.Rule<>(List.of(prod, 0.0d, List.of("?", "x")), 0.0d),
                new Operations.Rule<>(List.of(sum, List.of("?", "x"), 0.0d), List.of(":", "x")),
                new Operations.Rule<>(List.of(sum, 0.0d, List.of("?", "x")), List.of(":", "x")));
    }

    public final List<Operations.Rule<Object, Object>> PRODUCT_ALGEBRAIC_SIMPLIFICATION() {
        return List.of(
                new Operations.Rule<>( // c * (a+b) = c*a + c*b
                        List.of(prod, List.of("?", "c"), List.of(sum, List.of("?", "a"), List.of("?", "b"))),
                        List.of(sum, List.of(prod, List.of(":", "c"), List.of(":", "a")), List.of(prod, List.of(":", "c"), List.of(":", "b")))),
                new Operations.Rule<>( // (a+b) * c = a*c + b*c
                        List.of(prod, List.of(sum, List.of("?", "a"), List.of("?", "b")), List.of("?", "c")),
                        List.of(sum, List.of(prod, List.of(":", "a"), List.of(":", "c")), List.of(prod, List.of(":", "b"), List.of(":", "c"))))
        );
    }


}
