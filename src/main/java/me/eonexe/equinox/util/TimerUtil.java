package me.eonexe.equinox.util;

public class TimerUtil
{
    private long time;

    public TimerUtil() {
        this.time = -1L;
    }

    public boolean passedS(final double s) {
        return this.passedMs((long)s * 1000L);
    }

    public boolean passedDms(final double dms) {
        return this.passedMs((long)dms * 10L);
    }

    public boolean passedDs(final double ds) {
        return this.passedMs((long)ds * 100L);
    }

    public boolean passedMs(final long ms) {
        return this.passedNS(this.convertToNS(ms));
    }

    public void setMs(final long ms) {
        this.time = System.nanoTime() - this.convertToNS(ms);
    }

    public boolean passedNS(final long ns) {
        return System.nanoTime() - this.time >= ns;
    }

    public long getPassedTimeMs() {
        return this.getMs(System.nanoTime() - this.time);
    }

    public TimerUtil reset() {
        this.time = System.nanoTime();
        return this;
    }

    public long getMs(final long time) {
        return time / 1000000L;
    }

    public long convertToNS(final long time) {
        return time * 1000000L;
    }

    public boolean sleep(final long l) {
        if (System.nanoTime() / 1000000L - l >= l) {
            this.reset();
            return true;
        }
        return false;
    }
}