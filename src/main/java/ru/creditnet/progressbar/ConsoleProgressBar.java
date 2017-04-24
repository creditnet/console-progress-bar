package ru.creditnet.progressbar;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;

/**
 * The simplest console-based progress bar.
 *
 * @author antivoland
 */
public class ConsoleProgressBar implements Closeable {
    static final long DEFAULT_TICK_MILLIS = 1000;

    private final ProgressState state;
    private final ConsoleDrawer drawer;
    private final Thread worker;

    /**
     * Creates a progress bar with default print stream and default update interval.
     *
     * @param max maximum value
     */
    public ConsoleProgressBar(long max) {
        this(max, System.out, DEFAULT_TICK_MILLIS);
    }

    /**
     * Creates and starts a progress bar.
     *
     * @param max        maximum value
     * @param stream     print stream
     * @param tickMillis update interval in milliseconds
     */
    public ConsoleProgressBar(long max, PrintStream stream, long tickMillis) {
        if (max < 0) {
            throw new IllegalArgumentException("Max value must be non-negative");
        }
        if (stream == null) {
            throw new NullPointerException("Stream must not be null");
        }
        if (tickMillis <= 0) {
            throw new IllegalArgumentException("Tick millis must be positive");
        }
        this.state = new ProgressState(max);
        this.drawer = new ConsoleDrawer(stream);
        (this.worker = new Thread(() -> {
            while (true) {
                drawer.draw(state);
                try {
                    Thread.sleep(tickMillis);
                } catch (InterruptedException e) {
                    break;
                }
            }
        })).start();
    }

    /**
     * Advances this progress bar by one step.
     */
    public void step() {
        state.step();
    }

    /**
     * Advances this progress bar by a specific amount.
     *
     * @param delta step size
     */
    public void stepBy(long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("Delta must be non-negative");
        }
        state.stepBy(delta);
    }

    /**
     * Stops the progress bar.
     *
     * @throws IOException if an I/O error occurs
     */
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
