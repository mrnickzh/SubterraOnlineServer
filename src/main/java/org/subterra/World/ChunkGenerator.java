package org.subterra.World;

import org.subterra.Utils.DistributedRNG;

import java.util.HashMap;

public class ChunkGenerator {
    public ChunkGenerator() {}

    public Block[][] generateChunk(HashMap<Integer, Float> blockmap) {
        DistributedRNG drng = new DistributedRNG();
        for (int id : blockmap.keySet()) { drng.addNumber(id, blockmap.get(id)); }

        Block[][] chunk = new Block[16][16];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                int blockid = drng.getNumber();
                chunk[i][j] = new Block(i, j, blockid);
            }
        }
        return chunk;
    }
}
