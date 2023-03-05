package com.github.applejuiceyy.figurastockfish.stockfish.tree;

import com.github.applejuiceyy.figurastockfish.stockfish.StringReader;

public class LiteralNode extends Node<LiteralNode> {
    private final String name;

    public LiteralNode(String name) {
        super(LiteralParser::new);
        this.name = name;
    }

    static class LiteralParser extends NodeParser<LiteralNode> {
        public LiteralParser(ParsingContext context, LiteralNode literalNode) {
            super(context, literalNode);
        }

        @Override
        boolean test(StringReader reader) {
            return reader.expect(node.name) && (!reader.canRead(node.name.length() + 1) || reader.expect(node.name + " "));
        }

        @Override
        public void parse(StringReader reader) {
            reader.assert_(node.name);
            if (reader.canRead()) {
                reader.assert_(" ");
            }
            finish();
        }

        @Override
        public String getParsed() {
            return node.name;
        }

        @Override
        public String representation() {
            return String.format("\"%s\"", node.name);
        }
    }
}
