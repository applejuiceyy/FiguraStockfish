package com.github.applejuiceyy.figurastockfish.stockfish.processor;

import com.github.applejuiceyy.figurastockfish.stockfish.StringReader;
import com.github.applejuiceyy.figurastockfish.stockfish.data.PartialSearchInfo;
import com.github.applejuiceyy.figurastockfish.stockfish.data.SearchResults;
import com.github.applejuiceyy.figurastockfish.stockfish.tree.Holder;
import com.github.applejuiceyy.figurastockfish.stockfish.tree.Node;
import com.github.applejuiceyy.figurastockfish.stockfish.tree.ParsingContext;
import com.github.applejuiceyy.figurastockfish.stockfish.tree.StockfishError;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class AnalysisTask implements StockfishProcessor<SearchResults> {
    private final OutputStreamWriter writer;
    private CompletableFuture<SearchResults> results = null;
    private List<Consumer<PartialSearchInfo>> infoSubs = new ArrayList<>();
    private String collectedBestMove;
    private String collectedPonder;
    private BiFunction<String, StockfishProcessor<SearchResults>, CompletableFuture<SearchResults>> command;

    public AnalysisTask(OutputStreamWriter writer, BiFunction<String, StockfishProcessor<SearchResults>, CompletableFuture<SearchResults>> command) {
        this.writer = writer;
        this.command = command;
    }

    @Override
    public boolean shouldSendReadyOk() {
        return false;
    }

    @Override
    public boolean feed(StringReader reader) {
        AtomicReference<String> bestMove = new AtomicReference<>();
        AtomicReference<String> ponderMove = new AtomicReference<>();

        AtomicReference<String> depth = new AtomicReference<>();
        AtomicReference<String> seldepth = new AtomicReference<>();
        AtomicReference<String> time = new AtomicReference<>();
        AtomicReference<String> nodes = new AtomicReference<>();
        AtomicReference<String> pv = new AtomicReference<>();
        AtomicReference<String> multipv = new AtomicReference<>();
        AtomicReference<String> scoreCP = new AtomicReference<>();
        AtomicReference<String> scoreMate = new AtomicReference<>();
        AtomicReference<String> hashfull = new AtomicReference<>();
        AtomicReference<String> nps = new AtomicReference<>();
        AtomicReference<String> tbhits = new AtomicReference<>();
        AtomicReference<String> cpuload = new AtomicReference<>();

        Holder<?> root = Node.root()
                .with(
                        Node.literal("bestmove")
                                .mustConsumeChild()
                                .with(
                                        Node.string()
                                                .complete(bestMove::set)
                                                .with(Node.literal("ponder")
                                                        .mustConsumeChild()
                                                        .with(Node.string().complete(ponderMove::set))
                                                )
                                )
                )
                .with(
                        Node.literal("info")
                                .mustConsumeChild()
                                .with(Node.literal("depth").mustConsumeChild()
                                        .with(Node.integer().complete(depth::set))
                                )
                                .with(Node.literal("seldepth").mustConsumeChild()
                                        .with(Node.integer().complete(seldepth::set))
                                )
                                .with(Node.literal("time").mustConsumeChild()
                                        .with(Node.integer().complete(time::set))
                                )
                                .with(Node.literal("nodes").mustConsumeChild()
                                        .with(Node.integer().complete(nodes::set))
                                )
                                .with(Node.literal("pv").mustConsumeChild()
                                        .with(Node.string().complete(pv::set))
                                )
                                .with(Node.literal("multipv").mustConsumeChild()
                                        .with(Node.integer().complete(multipv::set))
                                )
                                .with(Node.literal("score").mustConsumeChild()
                                        .with(
                                                Node.literal("cp").mustConsumeChild()
                                                        .with(Node.integer().complete(scoreCP::set))
                                        )
                                        .with(
                                                Node.literal("mate").mustConsumeChild()
                                                        .with(Node.integer().complete(scoreMate::set))
                                        )
                                        .with(Node.literal("lowerbound"))
                                        .with(Node.literal("upperbound"))
                                )
                                .with(Node.literal("hashfull").mustConsumeChild()
                                        .with(Node.integer().complete(hashfull::set))
                                )
                                .with(Node.literal("nps").mustConsumeChild()
                                        .with(Node.integer().complete(nps::set))
                                )
                                .with(Node.literal("tbhits").mustConsumeChild()
                                        .with(Node.integer().complete(tbhits::set))
                                )
                                .with(Node.literal("cpuload").mustConsumeChild()
                                        .with(Node.integer().complete(cpuload::set))
                                )
                                .with(Node.literal("string").mustConsumeChild()
                                        .with(Node.greedyString())
                                )
                );

        ParsingContext ctx = new ParsingContext(reader, root);

        ctx.parse();

        if (bestMove.get() == null) {
            if (pv.get() != null) {
                PartialSearchInfo info = new PartialSearchInfo(
                        Integer.parseInt(depth.get()),
                        Integer.parseInt(nodes.get()),
                        pv.get(), scoreCP.get(), scoreMate.get());
                for (Consumer<PartialSearchInfo> infoSub : infoSubs) {
                    infoSub.accept(info);
                }
            }
        }
        else {
            collectedBestMove = bestMove.get();
            collectedPonder = ponderMove.get();
            return false;
        }

        return true;
    }

    @Override
    public SearchResults build() {
        return new SearchResults(collectedBestMove, collectedPonder);
    }

    @Contract("_->this")
    public AnalysisTask onUpdate(Consumer<PartialSearchInfo> info) {
        infoSubs.add(info);
        return this;
    }

    public void stop() {
        try {
            writer.write("stop\n");
            writer.flush();
        } catch (IOException e) {
            throw new StockfishError.ProcessExitedError();
        }
    }

    public CompletableFuture<SearchResults> start(String cmd) {
        if (results == null) {
            results = command.apply("go " + cmd, this);
        }
        return results;
    }
}
