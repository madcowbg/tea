package snippets;

import expressions.Op;
import matching.*;
import matching.simple.*;

import static matching.Operations.instantiate;
import static matching.Operations.match;

public class MainSimplifier {


    public static void main(String[] args) {
        var n5 = new SimpleAlgebra.Number(5);
        var n7 = new SimpleAlgebra.Number(7);
        var exp = new SimpleAlgebra.TripletExpression(Op.plus, n5, n7);

//        var pat = new SimpleCompositePattern(n5, n7);
        MatchedVariable x = new SimpleMatchedVariable("x");
        var patArbExp = new SimplePattern.SimpleArbitraryExpression(x);
        var pat = new SimplePattern.TripletPattern(Op.plus, patArbExp, n7);

        System.out.println(exp);
        System.out.println(pat);

        var emptyDict = SimpleDictionary.EMPTY;

        var dict = match(pat, exp, emptyDict);
        System.out.println(dict);

        var n3 = new SimpleAlgebra.Number(3);
        var sv = new SimpleSkeleton.SimpleSkeletonEvaluation(x);
        var s = new SimpleSkeleton.TripletSkeleton(Op.div, n3, sv);
        System.out.println(s);

        var instantiated = dict.map(d -> instantiate(d, s));
        System.out.println(instantiated);
    }

}
