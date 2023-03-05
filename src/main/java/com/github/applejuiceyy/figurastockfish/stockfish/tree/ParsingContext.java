package com.github.applejuiceyy.figurastockfish.stockfish.tree;

import com.github.applejuiceyy.figurastockfish.stockfish.StringReader;
import org.luaj.vm2.ast.Str;

import java.util.ArrayList;
import java.util.HashMap;

public class ParsingContext {
    public final StringReader reader;
    private final HashMap<Node<?>, Node.NodeParser<?>> content = new HashMap<>();
    ArrayList<Node<?>> stack = new ArrayList<>();
    Holder<?> root;

    public ParsingContext(StringReader reader, Holder<?> root) {
        this.reader = reader;
        this.root = root;
    }

    public boolean findCandidate() {
        Holder<?> current = stack.isEmpty() ? root : stack.get(stack.size() - 1);

        for (Node<?> child : current.children()) {
            Node.NodeParser<?> parser = getParser(child);

            if (!parser.isFinished() && parser.available() && parser.test(reader)) {
                if (current instanceof Node<?> node) {
                    getParser(node).finish();
                }

                stack.add(child);
                return true;
            }
        }

        if (current instanceof Node<?> node && !getParser(node).mustConsumeChildren()) {
            // don't use the current one
            for (int i = stack.size() - 2; i >= -1; i--) {
                Holder<?> parent = i >= 0 ? stack.get(i) : root;

                for (Node<?> child : parent.children()) {
                    if (child != stack.get(i + 1)) {
                        Node.NodeParser<?> parser = getParser(child);

                        if (!parser.isFinished() && parser.available() && parser.test(reader)) {
                            trimStack(i + 1);
                            stack.add(child);
                            return true;
                        }
                    }
                }
            }
        }
        if (current instanceof Node<?> node) {
            Node.NodeParser<?> parser = getParser(node);

            if (!parser.isFinished() && parser.test(reader)) {
                return true;
            }

            parser.finish();
        }

        if (current instanceof Node<?> node && getParser(node).mustConsumeChildren()) {
            reader.raise();
        }

        return false;
    }

    private void parseCandidate() {
        Node.NodeParser<?> parser = getParser(stack.get(stack.size() - 1));
        parser.parsing();
        parser.parse(reader);
    }

    public void parse() {
        while (findCandidate()) {
            parseCandidate();
        }
    }

    private void trimStack(int to) {
        while(to < stack.size()) {
            getParser(stack.get(stack.size() - 1)).finish();
            stack.remove(stack.size() - 1);
        }
    }

    public  <S extends Node<S>> Node.NodeParser<S> getParser(Node<S> node) {
        if (!content.containsKey(node)) {
            content.put(node, node.buildParser(this));
        }

        //noinspection unchecked
        return (Node.NodeParser<S>) content.get(node);
    }
}
