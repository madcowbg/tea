package matching;

public class Cons<U, V> {
    private final U head;
    private final V tail;

    public Cons(U head, V tail) {
        this.head = head;
        this.tail = tail;
    }

    public U car() {
        return head;
    }

    public V cdr() {
        return tail;
    }

    @Override
    public String toString() {
        return "(" + car().toString() + " " + cdr().toString() + ")";
    }
}
