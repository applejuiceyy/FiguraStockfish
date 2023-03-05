package com.github.applejuiceyy.figurastockfish.stockfish.tree;

import com.github.applejuiceyy.figurastockfish.stockfish.StringReader;

public class IntegerNode extends Node<IntegerNode> {
    public IntegerNode() {
        super(IntegerNode.IntegerParser::new);
    }

    static class IntegerParser extends NodeParser<IntegerNode> {
        String parsed;
        public IntegerParser(ParsingContext context, IntegerNode literalNode) {
            super(context, literalNode);
        }

        String tryParse(StringReader reader) {
            int p = reader.find(" ");
            String next;
            if (p == -1) {
                next = reader.getRest();
            }
            else {
                next = reader.peek(p - reader.getPos());
            }

            if (next.matches("^-?[0-9]+$")) {
                return next;
            }
            reader.hint("An Integer");
            return null;
        }

        @Override
        boolean test(StringReader reader) {
            return tryParse(reader) != null;
        }

        @Override
        public void parse(StringReader reader) {
            parsed = tryParse(reader);
            if (parsed != null) {
                reader.accept();
            }
            if (reader.canRead()) {
                reader.assert_(" ");
            }
            finish();
        }

        @Override
        public String getParsed() {
            return parsed;
        }

        @Override
        public String representation() {
            return "[integer]";
        }
    }
}