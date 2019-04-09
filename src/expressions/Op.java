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
        public double apply(double ...args) {
            assert args.length == 2;
            return args[0] + args[1];
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
        public double apply(double ...args) {
            assert args.length == 2;
            return args[0] - args[1];
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
        public double apply(double ...args) {
            assert args.length == 2;
            return args[0] * args[1];
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
        public double apply(double ...args) {
            assert args.length == 1;
            return Math.signum(args[0]);
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
        public double apply(double ...args) {
            assert args.length == 2;
            return args[0] / args[1];
        }
    }
}
