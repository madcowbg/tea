package matching.simple;

import functional.Maybe;
import matching.Dictionary;
import matching.Expression;
import matching.MatchedVariable;
import matching.Pattern;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static functional.Maybe.fail;
import static functional.Maybe.ok;

public class SimpleDictionary implements Dictionary {
    public static final Dictionary EMPTY = new SimpleDictionary(Collections.emptyMap());

    private final Map<MatchedVariable, Expression> mapping;

    private SimpleDictionary(Map<MatchedVariable, Expression> mapping) {
        this.mapping = mapping;
    }

    @Override
    public Maybe<Dictionary> extend(Pattern.ArbitraryConstant pat, Expression.Constant exp) {
        return checkExtending(pat.variable(), exp);
    }

    @Override
    public Maybe<Dictionary> extend(Pattern.ArbitraryVariable pat, Expression.Variable exp) {
        return checkExtending(pat.variable(), exp);
    }

    @Override
    public Maybe<Dictionary> extend(Pattern.ArbitraryExpression pat, Expression exp) {
        return checkExtending(pat.variable(), exp);
    }

    private Maybe<Dictionary> checkExtending(MatchedVariable variable, Expression exp) {
        if (mapping.containsKey(variable)) {
            return exp.isExpressionEqual(mapping.get(variable))
                    ? ok(this)
                    : fail("Can't match a second variable to " + variable.toString() + ": " + exp.toString() + "!=" + mapping.get(variable));
        } else {
            var extended = new HashMap<>(mapping);
            extended.put(variable, exp);
            return ok(new SimpleDictionary(extended));
        }
    }

    @Override
    public Expression lookup(MatchedVariable var) {
        if (!mapping.containsKey(var)) {
            throw new IllegalArgumentException("Variable not in dictionary! - " + var.toString());
        }
        return mapping.get(var);
    }

    @Override
    public String toString() {
        return mapping.entrySet().stream().map(e -> e.getKey() + " -> " + e.getValue()).collect(Collectors.joining("\n"));
    }
}
