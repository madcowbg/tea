package expressions;

public enum Op implements Printable, Operation {
    plus {
        @Override
        public String print() {
            return "+";
        }

        @Override
        public double apply(double a, double b) {
            return a + b;
        }
    },
    minus {
        @Override
        public String print() {
            return "-";
        }
        @Override
        public double apply(double a, double b) {
            return a - b;
        }
    },
    times {
        @Override
        public String print() {
            return "*";
        }
        @Override
        public double apply(double a, double b) {
            return a * b;
        }
    },
    div {
        @Override
        public String print() {
            return "/";
        }
        @Override
        public double apply(double a, double b) {
            return a / b;
        }
    }
}
