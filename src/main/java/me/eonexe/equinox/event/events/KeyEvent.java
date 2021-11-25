package me.eonexe.equinox.event.events;

import me.eonexe.equinox.event.EventStage;

public class KeyEvent
        extends EventStage {
    private final int key;

    public boolean info;
    public boolean pressed;

    public KeyEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

    public
    KeyEvent(int stage, int key, boolean info, boolean pressed) {
        super ( stage );
        this.key = key;
        this.info = info;
        this.pressed = pressed;
    }
}


