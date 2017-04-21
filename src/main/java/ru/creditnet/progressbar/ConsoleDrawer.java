package ru.creditnet.progressbar;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author antivoland
 */
class ConsoleDrawer implements Closeable {
    static final BiFunction<Long, Long, String> PERCENTAGE_FORMAT = (max, current) -> {
        if (max <= 0) return "     ";
        return String.format(" %1$3s%%", Math.round(100.0 * current / max));
    };

    static final BiFunction<Long, Long, String> ABSOLUTE_RATIO_FORMAT = (max, current) ->
            String.format("[ %d / %d ]", current, max);

    private static final Function<Duration, String> DURATION_FORMAT = duration -> {
        if (duration == null) return "?";
        long seconds = duration.getSeconds();
        return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, seconds % 60);
    };

    private static final BiFunction<Duration, Duration, String> DURATION_RATIO_FORMAT = (elapsed, estimated) ->
            String.format("%s / %s", DURATION_FORMAT.apply(elapsed), DURATION_FORMAT.apply(estimated));

    private final PrintStream stream;

    ConsoleDrawer(PrintStream stream) {
        this.stream = stream;
    }

    void draw(ProgressState state) {
        stream.print('\r');
        stream.print(PERCENTAGE_FORMAT.apply(state.max(), state.current()));
        stream.print(' ');
        stream.print(ABSOLUTE_RATIO_FORMAT.apply(state.max(), state.current()));
        stream.print(' ');
        stream.print(DURATION_RATIO_FORMAT.apply(state.elapsed(), state.estimated()));
    }

    @Override
    public void close() throws IOException {
        stream.print('\n');
    }
}
