package com.github.applejuiceyy.figurastockfish.stockfish.tree;

import com.github.applejuiceyy.figurastockfish.stockfish.StringReader;

public class GreedyString extends Node<GreedyString> {
    public GreedyString() {
        super(GreedyString.GreedyStringParser::new);
    }

    static class GreedyStringParser extends NodeParser<GreedyString> {
        String collected;

        public GreedyStringParser(ParsingContext context, GreedyString literalNode) {
            super(context, literalNode);
        }

        @Override
        boolean test(StringReader reader) {
            return true;
        }

        @Override
        public void parse(StringReader reader) {
            collected = reader.getRest();
            reader.setPos(reader.length());
            finish();
        }

        @Override
        public String getParsed() {
            return collected;
        }

        @Override
        public String representation() {
            return "[greedy]";
        }
    }
}