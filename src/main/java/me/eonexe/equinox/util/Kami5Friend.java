package me.eonexe.equinox.util;

import java.util.Objects;

public class Kami5Friend {
    String ign;

    public Kami5Friend(String ign) {
        this.ign = ign;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Kami5Friend Kami5Friend = (Kami5Friend)o;
        return this.ign.equals(Kami5Friend.ign);
    }

    public int hashCode() {
        return Objects.hash(this.ign);
    }

    public String toString() {
        return this.ign;
    }
}

