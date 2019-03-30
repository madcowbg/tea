package snippets;

import matching.Cons;
import matching.MatchedVariable;
import matching.Pattern;

class SimplePattern {
    static class SimpleCompositePattern extends Cons<Pattern, Pattern> implements Pattern.Composite {

        public SimpleCompositePattern(Pattern head, Pattern tail) {
            super(head, tail);
        }

        @Override
        public boolean isAtom() {
            return false;
        }
    }

    static class SimpleArbitraryExpression implements Pattern, Pattern.ArbitraryExpression {
        private final MatchedVariable var;

        public SimpleArbitraryExpression(MatchedVariable var) {
            this.var = var;
        }

        @Override
        public boolean isAtom() {
            return false;
        }

        @Override
        public MatchedVariable variable() {
            return var;
        }

        @Override
        public String toString() {
            return "(? " + var.toString() + ")";
        }
    }
}
