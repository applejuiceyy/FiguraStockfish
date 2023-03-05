package com.github.applejuiceyy.figurastockfish.stockfish;

import com.github.applejuiceyy.figurastockfish.stockfish.data.EngineInfo;
import com.github.applejuiceyy.figurastockfish.stockfish.processor.AnalysisTask;
import com.github.applejuiceyy.figurastockfish.stockfish.processor.ReadyProcessor;
import com.github.applejuiceyy.figurastockfish.stockfish.processor.StockfishProcessor;
import com.github.applejuiceyy.figurastockfish.stockfish.processor.UCICommandProcessor;
import com.github.applejuiceyy.figurastockfish.stockfish.tree.StockfishError;

import java.io.*;
import java.lang.ref.PhantomReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class StockfishInstance {
    static final HashMap<WeakReference<StockfishInstance>, Process> fishes = new HashMap<>();

    private final Process process;
    private final BufferedReader reader;
    private final OutputStreamWriter writer;

    public EngineInfo info = null;

    static {
        new Thread(() -> {
            // TODO: reference queue
            while (true) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    return;
                }

                synchronized (fishes) {
                    ArrayList<WeakReference<StockfishInstance>> dead = new ArrayList<>(2);
                    fishes.forEach((fish, process) -> {
                        if (fish.get() == null) {
                            dead.add(fish);
                            process.destroy();
                        }
                    });
                    for (WeakReference<StockfishInstance> deadFish : dead) {
                        fishes.remove(deadFish);
                    }
                }
            }
        }, "Stockfish Instance Cleaner").start();
    }

    private StockfishInstance(Process process, BufferedReader reader, OutputStreamWriter writer) {
        this.process = process;
        this.reader = reader;
        this.writer = writer;

        synchronized (fishes) {
            fishes.put(new WeakReference<>(this), process);
        }
    }

    static public CompletableFuture<StockfishInstance> bind(String cmd) {
        return CompletableFuture.supplyAsync(() -> {
            ProcessBuilder pb = new ProcessBuilder(cmd);

            try {
                Process process = pb.start();
                Thread.sleep(100);
                InputStream stream = process.getInputStream();
                while(stream.available() > 0) stream.skip(1);

                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
                StockfishInstance si = new StockfishInstance(process, reader, writer);

                try {
                    si.info = si.getInfo().get(3000, TimeUnit.MILLISECONDS);
                }
                catch (TimeoutException exc) {
                    process.destroy();
                    throw new RuntimeException("Program is not UCI or Timeout");
                }
                si.newGameHint().get();
                return si;
            } catch (ExecutionException e) {
                if (e.getCause() instanceof StockfishError v) {
                    throw v;
                }
                e.printStackTrace();
                return null;
            } catch (IOException | InterruptedException e) {
                throw new StockfishError.ProcessExitedError();
            }
        });
    }

    public <T> CompletableFuture<T> command(String command, StockfishProcessor<T> processor) {
        return CompletableFuture.supplyAsync(() -> {
            synchronized (this) {
                try {
                    writer.write(command);
                    writer.write("\n");
                    if (processor.shouldSendReadyOk()) {
                        writer.write("isready");
                        writer.write("\n");
                    }
                    writer.flush();
                } catch (IOException exception) {
                    close();
                    throw new StockfishError.ProcessExitedError();
                }

                while (true) {
                    String line;
                    try {
                        line = reader.readLine();
                    } catch (IOException e) {
                        throw new StockfishError.ProcessExitedError();
                    }
                    if (line == null) {
                        throw new StockfishError.ProcessExitedError();
                    }
                    if (line.length() != 0 && !processor.feed(new StringReader(line))) {
                        return processor.build();
                    }
                }
            }
        });
    }

    public CompletableFuture<EngineInfo> getInfo() {
        return command("uci", new UCICommandProcessor());
    }

    public CompletableFuture<Void> setFEN(String fen, String moves) {
        return command(String.format("position fen %s moves %s", fen, moves), ReadyProcessor.INSTANCE);
    }

    public CompletableFuture<Void> setFEN(String fen) {
        return command(String.format("position fen %s", fen), ReadyProcessor.INSTANCE);
    }

    public CompletableFuture<Void> newGameHint() {
        return command("ucinewgame", ReadyProcessor.INSTANCE);
    }

    public CompletableFuture<Void> setOption(String name, String value) {
        return command(String.format("setoption name %s value %s", name, value), ReadyProcessor.INSTANCE);
    }

    public AnalysisTask calculate() {
        return new AnalysisTask(writer, this::command);
    }

    public void close() {
        this.process.destroy();
    }
}
