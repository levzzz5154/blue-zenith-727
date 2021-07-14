package cat.util;

public final class MillisTimer {
    public long millis = -1L;

    public boolean hasTimeReached(final long ms) {
        return System.currentTimeMillis() >= millis + ms;
    }

    public long getTimeDiff(final long ms) {
        return (ms + millis) - System.currentTimeMillis();
    }

    public void reset() {
        millis = System.currentTimeMillis();
    }
}
