package sexpressions;

import java.util.List;

public interface Expressions<T> {
    interface Expr<T> {
        List<Expr<T>> children();
        T data();
    }

    Expr<T> cons(List<Expr<T>> children);
    Expr<T> atom(T apply);
    T data(String data);
}
