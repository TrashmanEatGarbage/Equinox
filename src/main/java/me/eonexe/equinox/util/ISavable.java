package me.eonexe.equinox.util;

import java.util.Map;

public interface ISavable {
    public void load(Map<String, Object> var1);

    public Map<String, Object> save();

    public String getFileName();

    public String getDirName();
}

