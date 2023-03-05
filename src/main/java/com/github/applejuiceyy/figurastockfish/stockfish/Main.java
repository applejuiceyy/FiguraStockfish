package com.github.applejuiceyy.figurastockfish.stockfish;

import com.github.applejuiceyy.figurastockfish.stockfish.data.SearchResults;
import com.github.applejuiceyy.figurastockfish.stockfish.processor.AnalysisTask;

import java.sql.Time;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // miscellaneous testing code
        StockfishInstance inst = StockfishInstance.bind("./run/stockfish.exe").get();
        inst.setFEN("startpos");
        AnalysisTask r = inst.calculate()
                .onUpdate(System.out::println);

        CompletableFuture<SearchResults> f = r.start("");

        Thread.sleep(1000);

        r.stop();

        SearchResults results = f.get();

        inst.close();
    }
}
