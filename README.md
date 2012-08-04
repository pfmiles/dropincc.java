dropincc.java Quick Start.
=============

### What is dropincc.java?
* A small, and easy to use parser generator.
* It is designed to ease your DSL building process in java.
* No new grammar need to learn, no additional command line tool need to use. All you need to do is define your DSL grammar in your favorite editor using pure java.
* It has very nice BNF-alike fluent interface API to define grammar rules.
* Recognizes LL(*) grammar, and supports powerful semantic predicates that could even handle context-sensitive parsing.
* No other dependencies except for the built-in java library, requires JDK 1.6 or above.

### Hello World.
As to parser generators, a full-featured calculator is more or less like a _Hello World_ example.  
Here is this resulting _Hello World_ example in dropincc.java:  

	/**
	 * EBNF of Calculator:
     * <pre>
     * calc ::= expr $
     * expr ::= addend (('+'|'-') addend)*
     * addend ::= factor (('*'|'/') factor)*
     * factor ::= '(' expr ')'
     *          | '\\d+(\\.\\d+)?'
     * </pre>
     */
    public static void main(String... args) throws Throwable {
        Lang c = new Lang("Calculator");
        Grule expr = c.newGrule();
        c.defineGrule(expr, CC.EOF).action(new Action() {
            public Double act(Object matched) {
                return (Double) ((Object[]) matched)[0];
            }
        });
        TokenDef a = c.newToken("\\+");
        Grule addend = c.newGrule();
        expr.define(addend, CC.ks(a.or("\\-"), addend)).action(new Action() {
            public Double act(Object matched) {
                Object[] ms = (Object[]) matched;
                Double a0 = (Double) ms[0];
                Object[] aPairs = (Object[]) ms[1];
                for (Object p : aPairs) {
                    String op = (String) ((Object[]) p)[0];
                    Double a = (Double) ((Object[]) p)[1];
                    if ("+".equals(op)) {
                        a0 += a;
                    } else {
                        a0 -= a;
                    }
                }
                return a0;
            }
        });
        TokenDef m = c.newToken("\\*");
        Grule factor = c.newGrule();
        addend.define(factor, CC.ks(m.or("/"), factor)).action(new Action() {
            public Double act(Object matched) {
                Object[] ms = (Object[]) matched;
                Double f0 = (Double) ms[0];
                Object[] fPairs = (Object[]) ms[1];
                for (Object p : fPairs) {
                    String op = (String) ((Object[]) p)[0];
                    Double f = (Double) ((Object[]) p)[1];
                    if ("*".equals(op)) {
                        f0 *= f;
                    } else {
                        f0 /= f;
                    }
                }
                return f0;
            }
        });
        factor.define("\\(", expr, "\\)").action(new Action() {
            public Double act(Object matched) {
                return (Double) ((Object[]) matched)[1];
            }
        }).alt("\\d+(\\.\\d+)?").action(new Action() {
            public Double act(Object matched) {
                return Double.parseDouble((String) matched);
            }
        });
        Exe exe = c.compile();
        System.out.println(exe.eval("1 +2+3+(4 +5*6*7*(64/8/2/(2/1 )/1)*8  +9  )+   10"));
    }

As you can see, these dozens of lines of pure java code defined a non-trivial calculator which supports parentheses operation.  
You can run the code above to get the output: `3389.0`. You can check this answer in the calculator app shipped along with your operation system.  

### How to build such a Calculator? Step by step:
First, write down the grammar rules as [EBNF](http://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_Form):   

	calc ::= expr $
    expr ::= addend (('+'|'-') addend)*
    addend ::= factor (('*'|'/') factor)*
    factor ::= '(' expr ')'
             | '\\d+(\\.\\d+)?'
             
Terminals are in single quotes and non-terminals are those meaningful words. '$' as the EOF terminal.  
Then, translate the EBNF **line-by-line** using dropincc.java API:

	Lang c = new Lang("Calculator");
    Grule expr = c.newGrule();
    // calc ::= expr $
    c.defineGrule(expr, CC.EOF);
    
    TokenDef a = c.newToken("\\+");
    Grule addend = c.newGrule();
    // expr ::= addend (('+'|'-') addend)*
    expr.define(addend, CC.ks(a.or("\\-"), addend));
    
    TokenDef m = c.newToken("\\*");
    Grule factor = c.newGrule();
    // addend ::= factor (('*'|'/') factor)*
    addend.define(factor, CC.ks(m.or("/"), factor));
    
    /*
     * factor ::= '(' expr ')'
     *          | '\\d+(\\.\\d+)?'
     */
    factor.define("\\(", expr, "\\)")
    .alt("\\d+(\\.\\d+)?");
    
It's a very straightforward step. Just need a little explaination:  

* Basic grammar elements are 'TokenDef'(terminal) and 'Grule'(non-terminal), and you are not restricted to define TokenDefs in prior to all other elements, you can just define TokenDefs instantly, as regular expression strings, while you defining structure of your grammar. Like `expr.define(addend, CC.ks(a.or("\\-"), addend));`, the `"\\-"` is a regex string, it defines a TokenDef on-the-fly.
* Elements concatenations are expressed in comma separated sequences, `expr, CC.EOF` means `expr $` for example.
* Alternative productions are expressed as a `alt` method call, for example, the `factor` rule has two alternative productions, the second one is defined as `.alt("\\d+(\\.\\d+)?");`.
* Inline alternatives are expressed as a `or` method call on elements. See `a.or("\\-")` or `m.or("/")` in above definitions.  
* Kleene star/cross notations is expressed as `CC.ks` and `CC.kc` function call.

Ok, as you have successfully translated EBNF to the form of dropincc.java required, it's time to add **actions** to your grammar rules.  

Well, those actions are in fact 'closures', it can catch-up any variables in your current context. In java, this is done by defining an anonymous inner class.  
For example, I first added two actions for the two alternative productions of `factor` rule: 

	factor.define("\\(", expr, "\\)").action(new Action() {
        public Double act(Object matched) {
            return (Double) ((Object[]) matched)[1];
        }
    }).alt("\\d+(\\.\\d+)?").action(new Action() {
        public Double act(Object matched) {
            return Double.parseDouble((String) matched);
        }
    });
    
`Action` is the interface to define such a closure. The `matched` param of the only method `act` in `Action` are an array of matched tokens or a single matched token.  
Whether it is array or single token is up to the number of elements in the corresponding alternative production:  

* The `matched` parameter in the action of the first alternative production is a three tokens array: \["(", returnedValueOfEnclosingExprRule, ")"\]
* The second alt's `matched` parameter is a single string object represents the matched digit.
* The return value of actions are treated as the returned value of the whole rule when parsing. It could be used for further processing by the invoking rule, for example, the second element of the first `matched` array is a returned value from the enclosing `expr` rule.  

When you have done adding all proper actions to all grammar rule's productions, you are almost winning. The last step is to compile your new created language:  

	Exe exe = c.compile();
	
Then, enjoy it:  

	System.out.println(exe.eval("1 +2+3+(4 +5*6*7*(64/8/2/(2/1 )/1)*8  +9  )+   10"));
	
There lies many staff of complexity in the implementation of dropincc.java, but to the end user, I believe it could turns out to be a form of simplicity.

That's it, dropincc.java, a new parser generator which you have never seen. It is not just 'yet another compiler compiler', because there is already a whole bunch of tools to do such kind of general purposed parser generation. It is aimed to help you create DSLs in java. Which is a neglected topic in java community. 

Oops.. I almost forgot, in order to make the example code above run, all you need to do is put the dropincc.java's jar file in your classpath, no other dependencies. 

More examples and documentation coming soon... You could explore the [wiki page](https://github.com/pfmiles/dropincc.java/wiki) or my [blog](http://pfmiles.github.com/blog/category/dropincc/) to find more information.

### Mail list and discussion forum

* [Dropincc Forum](http://dropincc-java.1068093.n5.nabble.com/). Click "Options > Post by email..." when you entered the forum to get the email address of our mailing list.

### NOTES

* dropincc.java is now in its very initial stage, so it has a lot of improvements to do: The token definition is implemented in java built-in regEx implementation, so it could not support 'longest match' which it is expected to be. It now behaviors like a 'earliest match' or something... So the token definition order matters a lot currently. This must be improved later.
* Due to some LL(*) issues, 'Non-LL regular' exceptions may be thrown when you defined some special case of rules coincidentally. This would be fixed soon.

### TODO

* TokenTypes in runtime lexing & parsing should be simplified to a 'hashCode & equals-efficient' type(int).
* Bootstrap "longest match" lexer regex engine.
* Backtrack when "non-LL Regular"
* Allow user to name each element for pretty printing.
* More examples and detailed documentation.
