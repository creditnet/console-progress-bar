package ru.creditnet.progressbar;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author antivoland
 */
public class ConsoleProgressBar implements Closeable {
    private static class State {
        private final long max;
        private final AtomicLong current = new AtomicLong();
        private final LocalDateTime start = LocalDateTime.now();

        State(long max) {
            if (max < 0) throw new IllegalArgumentException("Max value must be non-negative");
            this.max = max;
        }

        void step() {
            current.incrementAndGet();
        }

        void stepBy(long delta) {
            current.addAndGet(delta);
        }

        Duration elapsed() {
            return Duration.between(start, LocalDateTime.now());
        }

        Duration estimated() {
            if (max <= 0) return null;
            if (current.get() <= 0) return Duration.ZERO;
            return elapsed().dividedBy(current.get()).multipliedBy(max - current.get());
        }
    }

    private final State state;
    private final PrintStream stream;
    private final Thread worker = new Thread(() -> {
        while (true) {
            draw();
            try {
                Thread.sleep(1000); // todo: magic number
            } catch (InterruptedException e) {
                break;
            }
        }
    });

    public ConsoleProgressBar(long max) {
        this(max, System.out);
    }

    public ConsoleProgressBar(long max, PrintStream stream) {
        this.state = new State(max);
        this.stream = stream;
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
        draw();
        stream.print('\n');
    }

    private void draw() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
