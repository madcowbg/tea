package matching.simple;

import matching.Operations;
import matching.Operator;
import sexpressions.Reader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

;
public class Rules<Op extends Operator> {
    protected final BiFunction<String, String, Operations.Rule<Object, Object>> rulesReader;

    public Rules(Op[] ops) {
        this(ops, Collections.emptyList());
    }

    protected Rules(Op[] ops, List<Function<String, ?>> operatorTypeMaps) {

        var rules = Stream.concat(
                operatorTypeMaps.stream(),
                Stream.of(
                        Arrays.stream(ops).collect(Collectors.toMap(Object::toString, Function.identity()))::get,
                        (substituteOrEval) -> substituteOrEval.charAt(0) == ':' ? substituteOrEval : null,
                        (match) -> match.charAt(0) == '?' ? match : null,
                        String::new)).collect(Collectors.toList());

        var rulesConvertor = RulesReader.convertExpression(RulesReader.convertToken(rules));
        rulesReader = (pat, skel) ->
                new Operations.Rule<>(
                        rulesConvertor.apply(Reader.STRING.readSExp(pat).orElse((f) -> {throw new RuntimeException("Parsing pattern failed for " + pat + "with error " + f.toString());})),
                        rulesConvertor.apply(Reader.STRING.readSExp(skel).orElse((f) -> {throw new RuntimeException("Parsing skeleton failed for " + skel + "with error " + f.toString());})));
    }

    public final List<Operations.Rule<Object, Object>> ALGEBRAIC_NUMBER_EVALUATION_RULES() {
        return List.of(
                rulesReader.apply("(sgn (?c a))", "(:eval (sgn (: a)))"),
                rulesReader.apply("(+ (?c a) (?c b))", "(:eval (+ (: a) (: b)))"),
                rulesReader.apply("(* (?c a) (?c b))", "(:eval (* (: a) (: b)))"));
    }

    public final List<Operations.Rule<Object, Object>> ALGEBRAIC_SIMPLIFICATION_RULES() {
            return List.of(
                    rulesReader.apply("(* (? x) 1.0)", "(: x)"),
                    rulesReader.apply("(* 1.0 (? x))", "(: x)"),
                    rulesReader.apply("(* (? x) 0.0)", "0.0"),
                    rulesReader.apply("(* 0.0 (? x))", "0.0"),
                    rulesReader.apply("(+ 0.0 (? x))", "(: x)"),
                    rulesReader.apply("(+ (? x) 0.0)", "(: x)"));
    }

    public final List<Operations.Rule<Object, Object>> PRODUCT_ALGEBRAIC_SIMPLIFICATION() {
        return List.of(
                rulesReader.apply("(* (? c) (+ (? a) (? b)))", "(+ (* (: c) (: a)) (* (: c) (: b)))"),
                rulesReader.apply("(* (+ (? a) (? b)) (? c))", "(+ (* (: a) (: c)) (* (: b) (: c)))"),
                rulesReader.apply("(* (* (?c a) (? b)) (? c))", "(* (: a) (* (: b) (: c)))"),
                rulesReader.apply("(+ (+ (?c a) (? b)) (? c))", "(+ (: a) (+ (: b) (: c)))"));
    }


}
