package matching.simple;

import expressions.Op;
import matching.ExpressionSystem;
import matching.MatchedVariable;
import matching.PatternSystem;
import matching.SkeletonSystem;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SimpleAlgebra implements ExpressionSystem<Object, Object, Object>, PatternSystem<Object, Object, Object>, SkeletonSystem<Object, Object, Object, Object> {

    public enum Distribution {
        Normal {
            @Override
            public String toString() {
                return "N";
            }
        }
    }

    public static class Symbol {
        private final String representation;

        public Symbol(String representation) {
            assert representation != null;
            this.representation = representation;
        }

        @Override
        public String toString() {
            return representation;
        }
    }

    static final double TOL = 1e-12;

    @Override
    public Object car(Object exp) {
        assert exp instanceof List;
        return ((List) exp).get(0);
    }

    @Override
    public Object cdr(Object exp) {
        assert exp instanceof List;
        return ((List) exp).size() <= 1 ? null : ((List) exp).subList(1, ((List) exp).size());
    }

    @Override
    public boolean isAtom(Object a) {
        return !(a instanceof List);
    }

    @Override
    public boolean isAtomEqual(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        } else if (a instanceof Double && b instanceof Double) {
            return Math.abs(((Double) a) - ((Double) b)) < TOL;
        } else if (a instanceof Op && b instanceof Op) {
            return Objects.equals(a, b);
        } else if (a instanceof Variable && b instanceof Variable ) {
            return Objects.equals(a, b);
        } else if (a instanceof Distribution && b instanceof Distribution) {
            return Objects.equals(a, b);
        } else if (a instanceof Symbol && b instanceof Symbol) {
            return ((Symbol) a).representation.equals(((Symbol) b).representation);
        } else {
            return false;
        }
    }

    @Override
    public Object cons(Object a, Object b) {
        if (b == null) {
            return List.of(a);
        } else if (b instanceof List) {
            var op = ((List) b).get(0);
            if (op instanceof Distribution || op instanceof Op) {
                return List.of(a, b);
            } else {
                var res = new LinkedList<Object>((List)b);
                res.add(0, a);
                return res;
            }
        } else {
            return List.of(a, b);
        }
    }

    @Override
    public boolean isConstant(Object a) {
        return a instanceof Double;
    }

    @Override
    public boolean isVariable(Object a) {
        return a instanceof Variable;
    }

    @Override
    public boolean isCompositeExpression(Object a) {
        return a instanceof List;
    }

    @Override
    public boolean isExpressionEqual(Object a, Object b) {
        if (isAtom(a) && isAtom(b)) {
            return isAtomEqual(a, b);
        } else if (isCompositeExpression(a) && isCompositeExpression(b)) {
            return isExpressionEqual(car(a), car(b)) && isExpressionEqual(cdr(a), cdr(b));
        } else {
            return false;
        }
    }


    public static class Variable {
        private final String name;

        public Variable(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Variable && Objects.equals(this.name, ((Variable) obj).name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    public boolean isArbitraryConstant(Object p) {
        return p instanceof List && isArbitrary("?c", (List)p);
    }

    private boolean isArbitrary(String stringPattern, List p) {
        return p.size() == 2 && p.get(0).equals(stringPattern) && p.get(1) instanceof String;
    }

    @Override
    public boolean isArbitraryVariable(Object p) {
        return p instanceof List && isArbitrary("?v", (List)p);
    }

    @Override
    public boolean isArbitraryExpression(Object p) {
        return p instanceof List && isArbitrary("?", (List)p);
    }

    @Override
    public boolean isCompositePattern(Object p) {
        return p instanceof List;
    }

    @Override
    public MatchedVariable variableFrom(Object p) {
        assert p instanceof List && ((List) p).size() == 2;
        return new PatternVariable((String)(((List) p).get(1)));
    }


    public static class PatternVariable implements MatchedVariable{
        private final String name;

        public PatternVariable(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof PatternVariable && Objects.equals(this.name, ((PatternVariable) obj).name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    public boolean isSkeletonEvaluation(Object s) {
        return s instanceof List && ((List) s).size() == 2 && ((List) s).get(0).equals(":") && ((List) s).get(1) instanceof String;
    }

    @Override
    public MatchedVariable variableFromSkeletonEvaluation(Object s) {
        assert s instanceof List && ((List) s).size() == 2;
        return new PatternVariable((String)((List) s).get(1));
    }


    @Override
    public Object toAtom(Object o) {
        assert isAtom(o);
        return o;
    }
}
