package snippets;

public class MainTokenize {

    public static void main(String[] argc) {
        runTest("(+ ((? x) 7.0))");
        runTest("((+ ((? x) 7.0))");
        runTest("(+ ((? x) 7.0))(");
        runTest("(+ ((? x) 7.0)))");
        runTest(")(+ ((? x) 7.0))");
    }

    private static void runTest(String str) {
        System.out.println(str + " -> " + new SExpressionReader(str.toCharArray()).read());
    }
}
