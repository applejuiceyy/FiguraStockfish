package com.github.applejuiceyy.figurastockfish.stockfish;

import com.github.applejuiceyy.figurastockfish.stockfish.tree.StockfishError;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

public class StringReader {
    String string;
    int pos = 0;
    int consuming = 0;
    ArrayList<String> expected = new ArrayList<>();

    public StringReader(String str) {
        string = str;
    }

    public String getRest() {
        return string.substring(pos);
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int p) {
        pos = p;
    }

    public int find(String seq) {
        return string.indexOf(seq, pos);
    }

    public boolean canRead(int length) {
        return string.length() > pos + length - 1;
    }

    public boolean canRead() {
        return canRead(1);
    }

    public int length() {
        return string.length();
    }

    public boolean expect(String seq) {
        String cutout = peek(seq.length());
        expected.add(seq);
        return cutout.equals(seq);
    }

    public void assert_(String seq) {
        if (!expect(seq)) {
            raise();
        }
        accept();
    }

    public void accept() {
        pos += consuming;
        consuming = 0;
        expected.clear();
    }

    public String peek(int length) {
        consuming = length;
        return string.substring(pos, Math.min(length + pos, string.length()));
    }

    public void hint(String what) {
        expected.add(what);
    }

    @Contract("->fail")
    public void raise() {
        StringBuilder builder = new StringBuilder();
        int maxsize = 0;

        builder.append("Expected ");

        for (int i = 0; i < expected.size(); i++) {
            maxsize = Math.max(expected.get(i).length(), maxsize);
            builder.append('"');
            builder.append(expected.get(i));
            builder.append('"');

            if (i < expected.size() - 1) {
                builder.append(", ");
            }
        }

        builder.append("... Got ");
        String peek = peek(Math.max(maxsize, 10));
        if (peek.length() == 0) {
            builder.append("EOF");
        }
        else {
            builder.append(peek);
            builder.append("... (");
            builder.append(string.length() - getPos());
            builder.append(" remaining)");
        }

        builder.append(" (string \"");
        builder.append(string);
        builder.append("\")");

        throw new StockfishError.CommandSyntaxError(builder.toString());
    }
}
