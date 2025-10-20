package org.subterra.World;

import org.subterra.Utils.DistributedRNG;

public class ChunkGenerator {
    public DistributedRNG drng;

    public ChunkGenerator() {
        this.drng = new DistributedRNG();
        drng.addNumber(1, 0.7d);
        drng.addNumber(2, 0.3d);
    }

    public Block[][] generateChunk() {
        Block[][] chunk = new Block[64][64];
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                int blockid = drng.getNumber();
                chunk[i][j] = new Block(i, j, blockid);
            }
        }
        return chunk;
    }
}
