package ru.creditnet.progressbar;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author antivoland
 */
public class ConsoleProgressBarTest {
    private static final PrintStream DUMMY = new PrintStream(new ByteArrayOutputStream());

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMax() {
        new ConsoleProgressBar(-1, DUMMY, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullStream() {
        new ConsoleProgressBar(0, null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTickMillis() {
        new ConsoleProgressBar(0, DUMMY, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStepDelta() {
        new ConsoleProgressBar(0, DUMMY, 1).stepBy(-1);
    }
}
