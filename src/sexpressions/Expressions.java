package sexpressions;

import java.util.List;

public interface Expressions<T> {
    interface Expr {}

    Expr cons(List<Expr> children);
    Expr atom(T apply);
    T data(String data);
}
