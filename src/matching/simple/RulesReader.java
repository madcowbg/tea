package matching.simple;

import matching.Operator;
import sexpressions.Expressions;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RulesReader {
    public static <Token> Function<Token, Object> convertToken(List<Function<Token, ?>> operatorTypeMaps) {
        return token -> {
            try {
                return Double.valueOf(token.toString());
            } catch (NumberFormatException e) {
                return operatorTypeMaps.stream()
                        .map(f -> f.apply(token))
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Unrecognized token: " + token));
            }
        };
    }

    public static <Token> Function<Expressions.Expr<Token>, Object> convertExpression(Function<Token, Object> tokenConverter) {
        return (exp) -> {
            if (exp.data() != null) {// atom
                assert exp.children() == null;
                return tokenConverter.apply(exp.data());

            } else {
                assert exp.children() != null;
                List<Object> parsed = exp.children().stream().map(convertExpression(tokenConverter)).collect(Collectors.toList());
                if (parsed.get(0) instanceof Operator) {
                    return ((Operator) parsed.get(0)).of(parsed.stream().skip(1).toArray());
                } else {
                    return parsed; // FIXME remove when distributions are operators!
                }
            }
        };
    }
}
