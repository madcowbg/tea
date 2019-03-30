package matching;

import functional.Maybe;

import static functional.Maybe.fail;
import static functional.Maybe.ok;

public interface Operations {

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
            return ((Skeleton.Atom)s).toAtom();
        } else if (s.isSkeletonEvaluation()) {
            return dict.lookup(((Skeleton.SkeletonEvaluation) s).variable());
        } else {
            Skeleton.Composite cs = (Skeleton.Composite) s;
            return instantiate(dict, cs.car()).consWith(instantiate(dict, cs.cdr()));
        }
    }
}
