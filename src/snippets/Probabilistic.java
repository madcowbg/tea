package snippets;

import expressions.Op;
import matching.Operations;

import java.util.List;

import static matching.simple.SimpleAlgebra.Distribution.Normal;

public class Probabilistic {

    public final static List<Operations.Rule<Object, Object>> PROBABILISTIC_NORMAL_SIMPLIFIER_RULES = List.of(
            new Operations.Rule<>(
                    List.of(Op.mul, List.of("?c", "m"), List.of(Normal, List.of("?", "v"), List.of("?", "mu"), List.of("?", "sigma"))),
                    List.of(Normal, List.of(":", "v"), List.of(Op.mul, List.of(":", "m"), List.of(":", "mu")), List.of(Op.mul, List.of(":", "m"), List.of(":", "sigma")))),
            new Operations.Rule<>(
                    List.of(Op.plus, List.of("?c", "a"), List.of(Normal, List.of("?", "v"), List.of("?", "mu"), List.of("?", "sigma"))),
                    List.of(Normal, List.of(":", "v"), List.of(Op.plus, List.of(":", "a"), List.of(":", "mu")), List.of(":", "sigma"))),
            new Operations.Rule<>(
                    List.of(Op.mul, List.of("?d", "dist"), List.of("?c", "m")),
                    List.of(Op.mul, List.of(":", "m"), List.of(":", "dist"))),
            new Operations.Rule<>(
                    List.of(Op.plus, List.of("?d", "dist"), List.of("?c", "a")),
                    List.of(Op.plus, List.of(":", "a"), List.of(":", "dist"))),
            new Operations.Rule<>(List.of(Op.mul, List.of("?", "x"), 1.0d), List.of(":", "x")),
            new Operations.Rule<>(List.of(Op.mul, 1.0d, List.of("?", "x")), List.of(":", "x")),
            new Operations.Rule<>(List.of(Op.mul, List.of("?", "x"), 0.0d), 0.0d),
            new Operations.Rule<>(List.of(Op.mul, 0.0d, List.of("?", "x")), 0.0d),
            new Operations.Rule<>(List.of(Op.plus, List.of("?", "x"), 0.0d), List.of(":", "x")),
            new Operations.Rule<>(List.of(Op.plus, 0.0d, List.of("?", "x")), List.of(":", "x"))
    );
}
