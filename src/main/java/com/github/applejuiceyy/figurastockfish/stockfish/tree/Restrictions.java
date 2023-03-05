package com.github.applejuiceyy.figurastockfish.stockfish.tree;

import java.util.function.Predicate;

public class Restrictions {
    private Restrictions() {}

    static Predicate<ParsingContext> exclusive(Node<?> other) {
        return context -> !context.getParser(other).wasParsing();
    }


    public static void mutuallyExclusive(Node<?> one, Node<?> other) {
        one.restrict(exclusive(other));
        other.restrict(exclusive(one));
    }
}
