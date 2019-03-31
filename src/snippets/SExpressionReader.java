package snippets;

import functional.Maybe;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import static functional.Maybe.fail;
import static functional.Maybe.ok;
import static java.lang.Character.isWhitespace;

public class SExpressionReader {

    interface Expr {}

    private class Atom implements Expr {
        public final String data;

        public Atom(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return data;
        }
    }

    private class CompExp implements Expr {
        public final List<Expr> children;

        public CompExp(List<Expr> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return "(" + children.stream().map(Object::toString).collect(Collectors.joining(" ")) + ")";
        }
    }

    private final char[] s;

    public SExpressionReader(char[] s) {
        this.s = s;
    }

    public Maybe<Expr> read() {
        Stack<List<Expr>> partial = new Stack<>();
        partial.push(new LinkedList<>());

        int i = 0;
        while (true) {
            i = skipWS(i);
            if (i >= s.length) {
                break;
            } else if (s[i] == '(') {
                if (partial.size() == 1) {
                    if (partial.peek().size() != 0) {
                        return fail("can't have a second top-level expression at index " + i);
                    }
                }
                partial.push(new LinkedList<>());
                i++;
            } else if (s[i] == ')') {
                if (partial.size() == 1) {
                    return fail("unmatched closing bracket ')' at index " + i);
                }

                i++;
                List<Expr> children = partial.pop();
                partial.peek().add(new CompExp(children));
            } else {
                int start = i;
                int end = atomEnd(start);
                if (end > start) {
                    partial.peek().add(new Atom(String.copyValueOf(s, start, end - start)));
                }
                i = end;
            }
        }

        i = skipWS(i);
        if (i < s.length) {
            return fail("non-read characters after index " + i + ": " + String.copyValueOf(s, i, s.length - i));
        } else if (partial.size() > 1){
            return fail("non-terminated expressions of stack size " + (partial.size() - 1));
        } else {
            return ok(partial.peek().get(0));
        }
    }

    private int atomEnd(int start) {
        int end = start;
        while (end < s.length && !isWhitespace(s[end]) && s[end] != ')') {
            end++;
        }
        return end;
    }

    private int skipWS(int idx) {
        while(idx < s.length && isWhitespace(s[idx])) {idx++;}
        return idx;
    }

}
