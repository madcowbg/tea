package sexpressions;

import java.util.List;
import java.util.stream.Collectors;

class DefaultExpressions implements Expressions<String> {
    @Override
    public Expr cons(List<Expr> children) {
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

    private class Atom implements Expr {
        final String data;

        Atom(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    private class CompExp implements Expr {
        final List<Expr> children;

        CompExp(List<Expr> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "(" + children.stream().map(Object::toString).collect(Collectors.joining(" ")) + ")";
        }
    }
}
