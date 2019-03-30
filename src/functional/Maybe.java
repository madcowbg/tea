package functional;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface Maybe<T> {
    class Returns<T> implements Maybe<T> {
        private final T val;

        private Returns(T val) {
            this.val = val;
        }

        public <U> Maybe<U> map(Function<T, U> f) {
            return new Returns<>(f.apply(val));
        }

        // m a -> ( a -> m b) -> m b
        public <U> Maybe<U> bind(Function<T, Maybe<U>> f) {
            return f.apply(val);
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || (obj instanceof Returns && Objects.equals(((Returns)obj).val, this.val));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(val);
        }
    }

    class Fail<T> implements Maybe<T> {
        private final String reason;

        private Fail(String reason) {
            this.reason = reason;
        }

        public <U> Maybe<U> map(Function<T, U> f) {
            return new Fail<>(this.reason);
        }

        public <U> Maybe<U> bind(Function<T, Maybe<U>> f) {
            return new Fail<>(this.reason);
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || (obj instanceof Fail && Objects.equals(((Fail)obj).reason, this.reason));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(reason);
        }
    }

    <V> Maybe<V> map(Function<T, V> f);
    <U> Maybe<U> bind(Function<T, Maybe<U>> f);

    static <T> Maybe<T> ok(T val) {
        return new Returns<>(val);
    }

    static <T> Maybe<T> fail(String reason) {
        return new Fail<>(reason);
    }

    static <U, V, R> BiFunction<Maybe<U>, Maybe<V>, Maybe<R>> lift(BiFunction<U, V, R> f) {
        return (a, b) -> a.bind(av -> b.map(bv -> f.apply(av, bv)));
    }
}
