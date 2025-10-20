package org.subterra.Protocol.Packets;

import org.subterra.Protocol.Packet;
import org.subterra.Utils.ByteBuf;
import org.subterra.Utils.Vector2;

public class AddMapObject extends Packet {
    private int id;
    private Vector2 pos;
    private float rot;

    public AddMapObject(int id, Vector2 pos, float rot) {
        this.id = id;
        this.pos = pos;
        this.rot = rot;
    }
    @Override
    public void write(ByteBuf buffer) {
        buffer.writeInt(id);
        buffer.writeFloat(pos.x);
        buffer.writeFloat(pos.y);
        buffer.writeFloat(rot);
    }
}
