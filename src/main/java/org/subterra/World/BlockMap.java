package org.subterra.World;

import java.util.*;

public class BlockMap {
    HashMap<Float, HashMap<Integer, Float>> blockmap = new HashMap<>();

    public void append(Float index, HashMap<Integer, Float> chances) {
        this.blockmap.put(index, chances);
    }

    public HashMap<Integer, Float> get(float index) {
        float level = -100;
        ArrayList<Float> keys = new ArrayList<>(this.blockmap.keySet());
        keys.sort(null);
        for (float height : keys) {
            if (index < height) { break; }
            level = height;
            System.out.println(height);
        }
        System.out.println(level);
        return this.blockmap.get(level);
    }
}
