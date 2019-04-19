package sexpressions;

import java.util.List;
import java.util.stream.Collectors;

class DefaultExpressions implements Expressions<String> {
    @Override
    public Expr cons(List<Expr<String>> children) {
        return new CompExp(children);
    }

    @Override
    public Expr atom(String data) {
        return new Atom(data);
    }

    @Override
    public String data(String data) {
        return data;
    }

    private class Atom implements Expr<String> {
        final String data;

        Atom(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return data;
        }

        @Override
        public List<Expr<String>> children() {
            return null;
        }

        @Override
        public String data() {
            return data;
        }
    }

    private class CompExp implements Expr<String> {
        final List<Expr<String>> children;

        CompExp(List<Expr<String>> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "(" + children.stream().map(Object::toString).collect(Collectors.joining(" ")) + ")";
        }

        @Override
        public List<Expr<String>> children() {
            return children;
        }

        @Override
        public String data() {
            return null;
        }
    }
}
