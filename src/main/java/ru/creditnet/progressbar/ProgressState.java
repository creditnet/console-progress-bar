package ru.creditnet.progressbar;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author antivoland
 */
class ProgressState {
    private final long max;
    private final AtomicLong current = new AtomicLong();
    private final LocalDateTime start = LocalDateTime.now();

    ProgressState(long max) {
        if (max < 0) throw new IllegalArgumentException("Max value must be non-negative");
        this.max = max;
    }

    void step() {
        current.incrementAndGet();
    }

    void stepBy(long delta) {
        current.addAndGet(delta);
    }

    long max() {
        return max;
    }

    long current() {
        return current.get();
    }

    LocalDateTime start() {
        return start;
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
