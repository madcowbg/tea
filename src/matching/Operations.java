package matching;

import functional.Maybe;

import static functional.Maybe.fail;
import static functional.Maybe.ok;

public class Operations<Atom, Expression, CompositeExpression extends Expression, Pattern, CompositePattern extends Pattern, Skeleton, CompositeSkeleton extends Skeleton> {

    private final ExpressionSystem<Atom, Expression, CompositeExpression> e;
    private final PatternSystem<Atom, Pattern, CompositePattern> p;
    private final SkeletonSystem<Atom, Skeleton, CompositeSkeleton, Expression> s;

    public Operations(
            ExpressionSystem<Atom, Expression, CompositeExpression> e,
            PatternSystem<Atom, Pattern, CompositePattern> p,
            SkeletonSystem<Atom, Skeleton, CompositeSkeleton, Expression> s) {
        this.e = e;
        this.p = p;
        this.s = s;
    }

    @SuppressWarnings("unchecked")
    public Maybe<Dictionary<Expression>> match(Pattern pat, Expression exp, Dictionary<Expression> dict) {
        if (p.isAtom(pat)) {
            if (e.isAtom(exp)) {
                return p.isAtomEqual((Atom) pat, (Atom) exp)
                        ? ok(dict)
                        : fail("pattern and expression atoms are different: " + pat.toString() + " != " + exp.toString());
            } else {
                return fail("can't match pattern atom with expression non-atom: " + pat.toString() + " != " + exp.toString());
            }
        } else if (p.isArbitraryConstant(pat)) {
            return e.isConstant(exp)
                    ? dict.extend(e, p.variableFrom(pat), exp)
                    : fail("can't match constant pattern with non-constant expression: " + pat.toString() + " != " + exp.toString());
        } else if (p.isArbitraryVariable(pat)) {
            return e.isVariable(exp)
                    ? dict.extend(e, p.variableFrom(pat), exp)
                    : fail("can't match variable pattern witn non-variable expression: " + pat.toString() + " != " + exp.toString());
        } else if (p.isArbitraryExpression(pat)) {
            return dict.extend(e, p.variableFrom(pat), exp);
        } else if (e.isAtom(exp)) {
            return fail("can't match atom expression with pattern: " + pat.toString() + " != " + exp.toString());
        } else {
            if (e.isCompositeExpression(exp) && p.isCompositePattern(pat)) {
                return match(p.car((CompositePattern)pat), e.car((CompositeExpression) exp), dict)
                        .bind(dcar -> match(p.cdr((CompositePattern) pat), e.cdr((CompositeExpression) exp), dcar));
            } else {
                return fail("invalid expression type: " + exp.getClass().getSimpleName());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Expression instantiate(Dictionary<Expression> dict, Skeleton skeleton) {
        if (s.isAtom(skeleton)) {
            return s.toAtom(skeleton);
        } else if (s.isSkeletonEvaluation(skeleton)) {
            return dict.lookup(s.variableFromSkeletonEvaluation(skeleton));
        } else {
            return e.cons(instantiate(dict, s.car((CompositeSkeleton) skeleton)), instantiate(dict, s.cdr((CompositeSkeleton) skeleton)));
        }
    }
}
