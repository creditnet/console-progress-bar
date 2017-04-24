# Console progress bar

The simplest console-based progress bar with non-blocking interface for Java.

![Animated preview](bar.gif)

## Usage

```java
try (ConsoleProgressBar bar = new ConsoleProgressBar(someMax)) {
    some loop {
        // do some business
        bar.step(); // step by 1
        bar.stepBy(someDelta); // step by someDelta
    }
}
```
