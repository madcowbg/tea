package expressions;

public enum Op implements Printable, Operation {
    plus {
        @Override
        public String print() {
            return "+";
        }

        @Override
        public String toString() {
            return print();
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
        public String toString() {
            return print();
        }

        @Override
        public double apply(double a, double b) {
            return a - b;
        }
    },
    mul {
        @Override
        public String print() {
            return "*";
        }

        @Override
        public String toString() {
            return print();
        }

        @Override
        public double apply(double a, double b) {
            return a * b;
        }
    },
    sign{
        @Override
        public String print() {
            return "sgn";
        }

        @Override
        public String toString() {
            return print();
        }

        @Override
        public double apply(double a, double b) {
            return Math.signum(a);
        }
    },
    div {
        @Override
        public String print() {
            return "/";
        }

        @Override
        public String toString() {
            return print();
        }

        @Override
        public double apply(double a, double b) {
            return a / b;
        }
    }
}
