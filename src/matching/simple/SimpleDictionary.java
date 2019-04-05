package matching.simple;

import functional.Maybe;
import matching.Dictionary;
import matching.ExpressionSystem;
import matching.MatchedVariable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static functional.Maybe.fail;
import static functional.Maybe.ok;

public class SimpleDictionary<Expression> implements Dictionary<Expression> {
    public static final <Expression> Dictionary<Expression> EMPTY() {
        return new SimpleDictionary<>(Collections.emptyMap());
    }

    private final Map<MatchedVariable, Expression> mapping;

    private SimpleDictionary(Map<MatchedVariable, Expression> mapping) {
        this.mapping = mapping;
    }

    @Override
    public Maybe<Dictionary<Expression>> extend(ExpressionSystem<?, Expression, ? extends Expression> e, MatchedVariable var, Expression exp) {
        return checkExtending(e, var, exp);
    }

    private Maybe<Dictionary<Expression>> checkExtending(ExpressionSystem<?, Expression, ? extends Expression> e, MatchedVariable variable, Expression exp) {
        if (mapping.containsKey(variable)) {
            return e.isExpressionEqual(exp, mapping.get(variable))
                    ? ok(this)
                    : fail("Can't match a second variable to " + variable.toString() + ": " + exp.toString() + "!=" + mapping.get(variable));
        } else {
            var extended = new HashMap<>(mapping);
            extended.put(variable, exp);
            return ok(new SimpleDictionary<>(extended));
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
