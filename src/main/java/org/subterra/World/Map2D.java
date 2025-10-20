package org.subterra.World;

import org.subterra.Utils.Vector2;

import java.util.HashMap;

public class Map2D {
    HashMap<Vector2, Block[][]> map2d = new HashMap<>();

    public void append(Vector2 index, Block[][] chunk) {
        this.map2d.put(index, chunk);
    }

    public Block[][] get(Vector2 index) {
        return this.map2d.get(index);
    }
}
