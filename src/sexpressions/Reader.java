package sexpressions;

import functional.Maybe;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

import static functional.Maybe.fail;
import static functional.Maybe.ok;
import static java.lang.Character.isWhitespace;

public class Reader<T> {

    private final Expressions<T> tree;

    public final static Reader<String> STRING = new Reader<>(new DefaultExpressions());

    public Reader(Expressions<T> tree) {
        this.tree = tree;
    }

    public Maybe<Expressions.Expr<T>> readSExp(String str) {
        char[] s = str.toCharArray();
        Stack<List<Expressions.Expr<T>>> partial = new Stack<>();
        partial.push(new LinkedList<>());

        int i = 0;
        while (true) {
            i = skipWS(s, i);
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
                List<Expressions.Expr<T>> children = partial.pop();
                partial.peek().add(tree.cons(children));
            } else {
                int start = i;
                int end = atomEnd(s, start);
                if (end > start) {
                    partial.peek().add(tree.atom(tree.data(String.copyValueOf(s, start, end - start))));
                }
                i = end;
            }
        }

        i = skipWS(s, i);
        if (i < s.length) {
            return fail("non-readSExp characters after index " + i + ": " + String.copyValueOf(s, i, s.length - i));
        } else if (partial.size() > 1){
            return fail("non-terminated expressions of stack size " + (partial.size() - 1));
        } else {
            return ok(partial.peek().get(0));
        }
    }

    private int atomEnd(char[] s, int start) {
        int end = start;
        while (end < s.length && !isWhitespace(s[end]) && s[end] != ')') {
            end++;
        }
        return end;
    }

    private int skipWS(char[] s, int idx) {
        while(idx < s.length && isWhitespace(s[idx])) {idx++;}
        return idx;
    }
}
