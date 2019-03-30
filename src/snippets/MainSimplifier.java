package snippets;

import functional.Maybe;

import matching.*;

import static functional.Maybe.fail;
import static functional.Maybe.ok;

public class MainSimplifier {

    interface Skeleton {
        interface Composite extends Skeleton {
            Skeleton car();
            Skeleton cdr();

        }

        interface SkeletonEvaluation extends Skeleton {
            MatchedVariable variable();
        }

        default boolean isAtom() {
            return this instanceof Expression.Atom;
        }
        default boolean isSkeletonEvaluation() {
            return this instanceof SkeletonEvaluation;
        }

    }

    static Maybe<Dictionary> match(Pattern pat, Expression exp, Dictionary dict) {
        if (pat.isAtom()) {
            if (exp.isAtom()) {
                return ((Expression.Atom) pat).isAtomEqual((Expression.Atom) exp)
                        ? ok(dict)
                        : fail("pattern and expression atoms are different: " + pat.toString() + " != " + exp.toString());
            } else {
                return fail("can't match pattern atom with expression non-atom: " + pat.toString() + " != " + exp.toString());
            }
        } else if (pat.isArbitraryConstant()) {
            return exp.isConstant()
                    ? dict.extend((Pattern.ArbitraryConstant) pat, (Expression.Constant) exp)
                    : fail("can't match constant pattern with non-constant expression: " + pat.toString() + " != " + exp.toString());
        } else if (pat.isArbitraryVariable()) {
            return exp.isVariable()
                    ? dict.extend((Pattern.ArbitraryVariable) pat, (Expression.Variable) exp)
                    : fail("can't match variable pattern witn non-variable expression: " + pat.toString() + " != " + exp.toString());
        } else if (pat.isArbitraryExpression()) {
            return dict.extend((Pattern.ArbitraryExpression) pat, exp);
        } else if (exp.isAtom()) {
            return fail("can't match atom expression with pattern: " + pat.toString() + " != " + exp.toString());
        } else {
            if (exp.isCompositeExpression() && pat.isCompositePattern()) {
                Expression.Composite cexp = (Expression.Composite) exp;
                Pattern.Composite cpat = (Pattern.Composite) pat;
                return match(cpat.car(), cexp.car(), dict).bind(dcar -> match(cpat.cdr(), cexp.cdr(), dcar));
            } else {
                return fail("invalid expression type: " + exp.getClass().getSimpleName());
            }
        }
    }

    static Expression instantiate(Dictionary dict, Skeleton s) {
        if (s.isAtom()) {
            return (Expression.Atom) s;
        } else if (s.isSkeletonEvaluation()) {
            return dict.lookup(((Skeleton.SkeletonEvaluation) s).variable());
        } else {
            Skeleton.Composite cs = (Skeleton.Composite) s;
            return instantiate(dict, cs.car()).consWith(instantiate(dict, cs.cdr()));
        }
    }

    public static void main(String[] args) {
        var n5 = new SimpleAlgebra.SimpleNumber(5);
        var n7 = new SimpleAlgebra.SimpleNumber(7);
        var exp = n5.consWith(n7);

//        var pat = new SimpleCompositePattern(n5, n7);
        MatchedVariable x = new SimpleMatchedVariable("x");
        var patArbExp = new SimplePattern.SimpleArbitraryExpression(x);
        var pat = new SimplePattern.SimpleCompositePattern(patArbExp, n7);

        System.out.println(exp);
        System.out.println(pat);

        var emptyDict = SimpleDictionary.EMPTY;

        var dict = match(pat, exp, emptyDict);
        System.out.println(dict);

        var sv = new SimpleSkeletonEvaluation(x);
        var s = new SimpleCompositeSkeleton(n5, new SimpleCompositeSkeleton(sv, n7));
        System.out.println(s);

        var instantiated = dict.map(d -> instantiate(d, s));
        System.out.println(instantiated);
    }

    private static class SimpleCompositeSkeleton extends Cons<Skeleton, Skeleton> implements Skeleton.Composite {
        public SimpleCompositeSkeleton(Skeleton head, Skeleton tail) {
            super(head, tail);
        }
    }

    private static class SimpleSkeletonEvaluation implements Skeleton.SkeletonEvaluation {
        private final MatchedVariable var;

        public SimpleSkeletonEvaluation(MatchedVariable var) {
            this.var = var;
        }

        @Override
        public MatchedVariable variable() {
            return var;
        }

        @Override
        public String toString() {
            return "(: " + var.toString() + ")";
        }
    }
}
