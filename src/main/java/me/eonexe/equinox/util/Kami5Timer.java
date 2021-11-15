package me.eonexe.equinox.util;

public class Kami5Timer
{
    long startTime;
    long delay;
    boolean paused;

    public Kami5Timer() {
        this.startTime = System.currentTimeMillis();
        this.delay = 0L;
        this.paused = false;
    }

    public boolean isPassed() {
        return !this.paused && System.currentTimeMillis() - this.startTime >= this.delay;
    }

    public void resetDelay() {
        this.startTime = System.currentTimeMillis();
    }

    public void setDelay(final long delay) {
        this.delay = delay;
    }

    public void setPaused(final boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public long getStartTime() {
        return this.startTime;
    }
}
