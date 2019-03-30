package matching.simple;

import matching.MatchedVariable;

import java.util.Objects;

public class SimpleMatchedVariable implements MatchedVariable {
    private final String name;
    public SimpleMatchedVariable(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof SimpleMatchedVariable && Objects.equals(((SimpleMatchedVariable) obj).name, name));
    }

    @Override
    public String toString() {
        return name;
    }
}
