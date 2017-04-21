package ru.creditnet.progressbar;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author antivoland
 */
public class ConsoleProgressBar implements Closeable {
    private static final long DEFAULT_TICK_MILLIS = 1000;

    private final ProgressState state;
    private final ConsoleDrawer drawer;
    private final Thread worker;

    public ConsoleProgressBar(long max) {
        this(max, System.out, DEFAULT_TICK_MILLIS);
    }

    public ConsoleProgressBar(long max, PrintStream stream, long tickMillis) {
        if (tickMillis <= 0) throw new IllegalArgumentException("Tick millis must be positive");
        this.state = new ProgressState(max);
        this.drawer = new ConsoleDrawer(stream);
        this.worker = new Thread(() -> {
            while (true) {
                drawer.draw(state);
                try {
                    Thread.sleep(tickMillis);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }

    public void step() {
        state.step();
    }

    public void stepBy(long delta) {
        state.stepBy(delta);
    }

    @Override
    public void close() throws IOException {
        worker.interrupt();
        try {
            worker.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        drawer.draw(state);
        drawer.close();
    }
}
