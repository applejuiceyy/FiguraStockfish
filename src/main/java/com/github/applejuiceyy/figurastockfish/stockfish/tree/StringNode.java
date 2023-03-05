package com.github.applejuiceyy.figurastockfish.stockfish.tree;

import com.github.applejuiceyy.figurastockfish.stockfish.StringReader;

public class StringNode extends Node<StringNode>{
    private final boolean canBeEmpty;

    public StringNode(boolean canBeEmpty) {
        super(StringParser::new);
        this.canBeEmpty = canBeEmpty;
    }

    static class StringParser extends Node.NodeParser<StringNode> {
        StringBuilder string = new StringBuilder();
        String finished = null;

        public StringParser(ParsingContext context, StringNode literalNode) {
            super(context, literalNode);
        }

        @Override
        boolean test(StringReader reader) {
            if (node.canBeEmpty) {
                return true;
            }
            reader.hint("not EOF");
            return reader.canRead();
        }

        @Override
        void finish() {
            finished = string.toString();
            super.finish();
        }

        @Override
        public void parse(StringReader reader) {
            int p;
            if (!reader.canRead()) {
                finish();
                return;
            }
            if (!string.isEmpty()) {
                string.append(' ');
            }
            if((p = reader.find(" ")) == -1) {
                string.append(reader.getRest());
                reader.setPos(reader.length());
            }
            else {
                string.append(reader.peek(p - reader.getPos()));
                reader.accept();
                reader.peek(1);
                reader.accept();
            }
        }

        @Override
        public String getParsed() {
            return isFinished() ? finished : null;
        }

        @Override
        public String representation() {
            return "[string]";
        }
    }
}
