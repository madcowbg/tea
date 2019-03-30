package algebra;

import java.util.Objects;

public class SimpleVariable implements Algebra.Variable {
    private final String name;

    public SimpleVariable(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof SimpleVariable && Objects.equals(((SimpleVariable)obj).name, name));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean isNumber() {
        return false;
    }

    @Override
    public boolean isVariable() {
        return true;
    }

    @Override
    public boolean isSum() {
        return false;
    }

    @Override
    public boolean isProduct() {
        return false;
    }

    @Override
    public boolean areSameVariableAs(Algebra.Variable other) {
        return this.equals(other);
    }

    @Override
    public String toString() {
        return name;
    }
}
