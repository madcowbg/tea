package matching.simple;

import expressions.Operation;
import matching.Operator;

public enum Op implements Operation, Operator {
    prod {
        @Override
        public double apply(double... args) {
            return args[0] * args[1];
        }

        @Override
        public String toString() {
            return "*";
        }
    }, sign {
        @Override
        public double apply(double... args) {
            return Math.signum(args[0]);
        }
        @Override
        public String toString() {
            return "sgn";
        }
    }, sum {
        @Override
        public double apply(double... args) {
            return args[0] + args[1];
        }

        @Override
        public String toString() {
            return "+";
        }
    }
}
