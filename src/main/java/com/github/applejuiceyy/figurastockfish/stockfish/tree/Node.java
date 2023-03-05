package com.github.applejuiceyy.figurastockfish.stockfish.tree;

import com.github.applejuiceyy.figurastockfish.stockfish.StringReader;
import org.apache.logging.log4j.util.BiConsumer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class Node<S extends Node<S>> extends Holder<S> {
    private final BiFunction<ParsingContext, S, NodeParser<S>> builder;
    final List<Predicate<ParsingContext>> restrictions = new ArrayList<>(0);
    final List<BiConsumer<String, ParsingContext>> completions = new ArrayList<>(0);
    final List<Consumer<String>> scompletions = new ArrayList<>(0);
    boolean mustConsumeChildren = false;

    protected Node(BiFunction<ParsingContext, S, NodeParser<S>> builder) {
        this.builder = builder;
    }

    public S restrict(Predicate<ParsingContext> restriction) {
        restrictions.add(restriction);
        return getThis();
    }

    public S complete(BiConsumer<String, ParsingContext> consumer) {
        completions.add(consumer);
        return getThis();
    }

    public S complete(Consumer<String> consumer) {
        scompletions.add(consumer);
        return getThis();
    }

    public S mustConsumeChild() {
        mustConsumeChildren = true;
        return getThis();
    }

    NodeParser<S> buildParser(ParsingContext context) {
        return builder.apply(context, getThis());
    }

    @Contract("_ -> new")
    public static @NotNull LiteralNode literal(String name) {
        return new LiteralNode(name);
    }

    @Contract(" -> new")
    public static @NotNull Holder<?> root() {
        return new Holder<>();
    }

    @Contract(" -> new")
    public static @NotNull StringNode string() {
        return new StringNode(false);
    }

    @Contract(" -> new")
    public static @NotNull StringNode emptyString() {
        return new StringNode(true);
    }

    @Contract(" -> new")
    public static @NotNull GreedyString greedyString() {
        return new GreedyString();
    }

    @Contract(" -> new")
    public static @NotNull IntegerNode integer() {
        return new IntegerNode();
    }

    public static abstract class NodeParser<S extends Node<S>> {
        protected final ParsingContext context;
        protected final S node;

        private boolean finished = false;
        private boolean parsing = false;

        public NodeParser(ParsingContext context, S literalNode) {
            this.context = context;
            this.node = literalNode;
        }

        void finish() {
            finished = true;
            callCompletionHook();
        }
        void parsing() {
            parsing = true;
        }

        public boolean isFinished() {
            return finished;
        }

        boolean wasParsing() {
            return parsing;
        }

        boolean mustConsumeChildren() {
            return node.mustConsumeChildren;
        }

        void callCompletionHook() {
            String parse = getParsed();
            for (BiConsumer<String, ParsingContext> completion : node.completions) {
                completion.accept(parse, context);
            }
            for (Consumer<String> scompletion : node.scompletions) {
                scompletion.accept(parse);
            }
        }

        boolean available() {
            for (Predicate<ParsingContext> completion : node.restrictions) {
                if (!completion.test(context)) {
                    return false;
                };
            }
            return true;
        }

        @Contract(pure = true)
        abstract boolean test(StringReader reader);
        abstract public void parse(StringReader reader);
        abstract public String getParsed();
        abstract public String representation();
    }
}
