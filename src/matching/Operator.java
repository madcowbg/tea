package matching;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Operator {

    default List of(Object... args) {
        assert args.length == arity(): "arity of '" + this.toString() + "' is " + arity() + " but called with " + args.length + " arguments!";
        return Stream.concat(Stream.of(this), Stream.of(args)).collect(Collectors.toList());
    }

    int arity();
}
