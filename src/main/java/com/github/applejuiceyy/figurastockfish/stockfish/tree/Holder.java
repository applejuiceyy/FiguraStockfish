package com.github.applejuiceyy.figurastockfish.stockfish.tree;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Holder<S extends Holder<S>> {
    private final ArrayList<Node<?>> children = new ArrayList<>();

    @Contract("_->this")
    public S with(Node<?> n) {
        children.add(n);
        return getThis();
    }

    public S getThis() {
        //noinspection unchecked
        return (S) this;
    }

    List<Node<?>> children() {
        return Collections.unmodifiableList(children);
    }
}
