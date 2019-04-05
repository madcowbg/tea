package snippets;

import expressions.Op;
import matching.*;
import matching.simple.*;

import java.util.List;

public class MainSimplifier {


    public static void main(String[] args) {
        var a = new SimpleAlgebra();
        Double n5 = 5.0;
        Double n7 = 7.0;
        var exp = List.of(Op.plus, n5, n7);

        var patArbExp = List.of("?", "x");
        var pat = List.of(Op.plus, patArbExp, n7);

        System.out.println(exp);
        System.out.println(pat);

        var emptyDict = SimpleDictionary.EMPTY();

        var o = new Operations<>(a, a, a);
        var dict = o.match(pat, exp, emptyDict);
        System.out.println(dict);

        var n3 = 3.0;
        var s = List.of(Op.div, n3, List.of(":", "x"));
        System.out.println(s);

        var instantiated = dict.map(d -> o.instantiate(d, s));
        System.out.println(instantiated);
    }

}
