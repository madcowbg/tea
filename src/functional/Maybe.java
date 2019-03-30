package functional;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

interface Monad<T> {
    <V> Maybe<V> map(Function<T, V> f);
    <U> Maybe<U> bind(Function<T, Maybe<U>> f);
}

public interface Maybe<T> extends Monad<T> {
    class Returns<T> implements Maybe<T> {
        private final T val;

        private Returns(T val) {
            this.val = val;
        }

        @Override
        public <U> Maybe<U> map(Function<T, U> f) {
            return new Returns<>(f.apply(val));
        }

        // m a -> ( a -> m b) -> m b
        @Override
        public <U> Maybe<U> bind(Function<T, Maybe<U>> f) {
            return f.apply(val);
        }

        @Override
        public T orElse(Function<Fail<T>, T> f) {
            return val;
        }

        @Override
        public T orElse(T other) {
            return val;
        }

        @Override
        public <E extends Exception> T orElseThrow(Supplier<E> e) {
            return val;
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || (obj instanceof Returns && Objects.equals(((Returns)obj).val, this.val));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(val);
        }

        @Override
        public String toString() {
            return "ok[" + val + "]";
        }
    }

    class Fail<T> implements Maybe<T> {
        private final String reason;

        private Fail(String reason) {
            this.reason = reason;
        }

        @Override
        public <U> Maybe<U> map(Function<T, U> f) {
            return new Fail<>(this.reason);
        }

        @Override
        public <U> Maybe<U> bind(Function<T, Maybe<U>> f) {
            return new Fail<>(this.reason);
        }

        @Override
        public T orElse(Function<Fail<T>, T> f) {
            return f.apply(this);
        }

        @Override
        public T orElse(T other) {
            return other;
        }

        @Override
        public <E extends Exception> T orElseThrow(Supplier<E> e) throws E {
            throw e.get();
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || (obj instanceof Fail && Objects.equals(((Fail)obj).reason, this.reason));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(reason);
        }

        @Override
        public String toString() {
            return "fail[" + reason + "]";
        }
    }

    T orElse(Function<Fail<T>, T> f);
    T orElse(T other);
    <E extends Exception> T orElseThrow(Supplier<E> e) throws E;

    static <T> Maybe<T> ok(T val) {
        return new Returns<>(val);
    }

    static <T> Maybe<T> fail(String reason) {
        return new Fail<>(reason);
    }

    static <U, V, R> BiFunction<Maybe<U>, Maybe<V>, Maybe<R>> lift(BiFunction<U, V, R> f) {
        return (a, b) -> a.bind(av -> b.map(bv -> f.apply(av, bv)));
    }

    static <U> Function<U, Maybe<U>> repeat(Function<U, Maybe<U>> f, int ntimes) {
        if (ntimes < 1) {
            throw new IllegalArgumentException();
        } else if (ntimes == 1) {
            return f;
        } else {
            return (u) -> f.apply(u).bind(repeat(f, ntimes-1));
        }
    }
}
