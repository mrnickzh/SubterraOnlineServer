package org.subterra.Protocol.Packets;

import org.subterra.Protocol.Packet;
import org.subterra.Utils.ByteBuf;

public class AddMapObject extends Packet {
    private int id;
    private float[] pos;
    private float[] rot;

    public AddMapObject(int id, float[] pos, float[] rot) {
        this.id = id;
        this.pos = pos;
        this.rot = rot;
    }
    @Override
    public void write(ByteBuf buffer) {
        buffer.writeInt(id);
        buffer.writeFloat(pos[0]);
        buffer.writeFloat(pos[1]);
        buffer.writeFloat(pos[2]);
        buffer.writeFloat(rot[0]);
        buffer.writeFloat(rot[1]);
        buffer.writeFloat(rot[2]);
    }
}
