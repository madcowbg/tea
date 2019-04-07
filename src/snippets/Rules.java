package snippets;

import expressions.Op;
import matching.Operations;

import java.util.List;
import static matching.simple.SimpleAlgebra.Distribution.Normal;

public class Rules {
    public static List<Operations.Rule<Object, Object>> ALGEBRAIC_NUMBER_EVALUATION_RULES = List.of(
            new Operations.Rule<>( // eval sign(b)
                    List.of(Op.sign, List.of("?c", "a")),
                    List.of(":eval", List.of(Op.sign, List.of(":", "a")))),
            new Operations.Rule<>( // eval a + b
                    List.of(Op.plus, List.of("?c", "a"), List.of("?c", "b")),
                    List.of(":eval", List.of(Op.plus, List.of(":", "a"), List.of(":", "b")))),
            new Operations.Rule<>( // eval a * b
                    List.of(Op.mul, List.of("?c", "a"), List.of("?c", "b")),
                    List.of(":eval", List.of(Op.mul, List.of(":", "a"), List.of(":", "b"))))
    );

    public final static List<Operations.Rule<Object, Object>> ALGEBRAIC_SIMPLIFICATION_RULES = List.of(
            new Operations.Rule<>(List.of(Op.mul, List.of("?", "x"), 1.0d), List.of(":", "x")),
            new Operations.Rule<>(List.of(Op.mul, 1.0d, List.of("?", "x")), List.of(":", "x")),
            new Operations.Rule<>(List.of(Op.mul, List.of("?", "x"), 0.0d), 0.0d),
            new Operations.Rule<>(List.of(Op.mul, 0.0d, List.of("?", "x")), 0.0d),
            new Operations.Rule<>(List.of(Op.plus, List.of("?", "x"), 0.0d), List.of(":", "x")),
            new Operations.Rule<>(List.of(Op.plus, 0.0d, List.of("?", "x")), List.of(":", "x")));

    public final static List<Operations.Rule<Object, Object>> PRODUCT_ALGEBRAIC_SIMPLIFICATION = List.of(
            new Operations.Rule<>( // c * (a+b) = c*a + c*b
                    List.of(Op.mul, List.of("?", "c"), List.of(Op.plus, List.of("?", "a"), List.of("?", "b"))),
                    List.of(Op.plus, List.of(Op.mul, List.of(":", "c"), List.of(":", "a")), List.of(Op.mul, List.of(":", "c"), List.of(":", "b")))),
            new Operations.Rule<>( // (a+b) * c = a*c + b*c
                    List.of(Op.mul, List.of(Op.plus, List.of("?", "a"), List.of("?", "b")), List.of("?", "c")),
                    List.of(Op.plus, List.of(Op.mul, List.of(":", "a"), List.of(":", "c")), List.of(Op.mul, List.of(":", "b"), List.of(":", "c"))))
    );


}
