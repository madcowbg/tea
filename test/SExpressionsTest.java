import functional.Maybe;
import org.testng.Assert;
import org.testng.annotations.Test;
import sexpressions.Reader;

@Test
public class SExpressionsTest {
    @Test
    public void CorrectExpressionsParseCorrectly() {
        assertCorrect("(+ ((? x) 7.0))");
        assertCorrect("(+ ((? x) as7.0))");
        assertCorrect("(+ a b (* c d e f) ( - g) (+ +))");
    }

    public void IncorrectExpressionsParsingFails() {
        assertFail("((+ ((? x) 7.0))");
        assertFail("(+ ((? x) 7.0))(");
        assertFail("(+ ((? x) 7.0)))");
        assertFail(")(+ ((? x) 7.0))");
    }

    private void assertFail(String str) {
        var e = Reader.STRING.readSExp(str);
        Assert.assertTrue(e instanceof Maybe.Fail);
    }

    private void assertCorrect(String str) {
        Assert.assertEquals(Reader.STRING.readSExp(str).orElseThrow(RuntimeException::new) + "", cleanSexprString(str));
    }

    private String cleanSexprString(String str) {
        return str.replaceAll("\\s+", " ").replaceAll("\\(\\s*", "(").replaceAll("\\s*\\)", ")");
    }
}
