package com.github.applejuiceyy.figurastockfish.stockfish.tree;

public class StockfishError extends Error {
    public StockfishError(String format) {
        super(format);
    }

    public static class CommandSyntaxError extends StockfishError {
        public CommandSyntaxError(String format) {
            super(format);
        }
    }
    public static class ProcessExitedError extends StockfishError {
        public ProcessExitedError() {
            super("Process Exited");
        }
    }
}
