package org.subterra.Protocol.Packets;

import org.subterra.ClientSession;
import org.subterra.Main;
import org.subterra.Protocol.Packet;
import org.subterra.Protocol.PacketsHelper;
import org.subterra.Utils.ByteBuf;
import org.subterra.Utils.Vector2;
import org.subterra.World.Block;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class GenerateChunk extends Packet {
    Vector2 chunkpos;

    public GenerateChunk() {}
    public GenerateChunk(float cx, float cy) {
        this.chunkpos = new Vector2(cx, cy);
    }

    @Override
    public void write(ByteBuf buffer) {
        Block[][] chunk;

        chunk = Main.worldChunks.get(chunkpos);
        System.out.println(chunk == null);
        if (chunk == null) {
            System.out.println("Chunk " + chunkpos.x + " " + chunkpos.y + " requested");
            chunk = Main.chunkgen.generateChunk(Main.blockmap.get(chunkpos.y));
            Main.worldChunks.append(chunkpos, chunk);
            System.out.println("Chunk " + chunkpos.x + " " + chunkpos.y + " generated");
        }

        buffer.writeFloat(chunkpos.x);
        buffer.writeFloat(chunkpos.y);

        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 16; j++){
                buffer.writeInt(chunk[i][j].id);
                buffer.writeFloat(chunk[i][j].x);
                buffer.writeFloat(chunk[i][j].y);
            }
        }
    }

    @Override
    public void read(ByteBuf buffer) {
        this.chunkpos = new Vector2(buffer.readFloat(), buffer.readFloat());
    }

    @Override
    public void process(ClientSession client) {
        PacketsHelper.send(client, new GenerateChunk(this.chunkpos.x, this.chunkpos.y));
    }
}
