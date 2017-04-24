package ru.creditnet.progressbar;

import org.junit.Test;

/**
 * @author antivoland
 */
public class ConsoleProgressBarTest {
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMax() {
        new ConsoleProgressBar(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullStream() {
        new ConsoleProgressBar(0, null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTickMillis() {
        new ConsoleProgressBar(0, System.out, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStepDelta() {
        new ConsoleProgressBar(Long.MAX_VALUE).stepBy(-1);
    }
}
