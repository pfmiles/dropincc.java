dropincc.java
=============

### Specs.

* A small, and easy to use compiler generator;
* It could build lexer & grammar rules on the fly and compiles at any time;
* It does not require you to learn another DSL for writing down lexer & grammer rules and provide a corresponding compiler to parse your rules. It just use java so it's ALL java(both its implementation and usage) and you can craft your own language at any time, anywhere in your application;
* It does not depend on any other third-party libs but just the JDK1.6(or above) library.
* It recognizes LL(*) grammer.

### TODO

* TokenTypes in runtime lexing & parsing should be simplified to a 'hashCode & equals-efficient' type(int).
* Bootstrap "longest match" lexer regex engine.
* Allow user to name each element for pretty printing.

