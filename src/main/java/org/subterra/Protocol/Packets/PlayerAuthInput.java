package org.subterra.Protocol.Packets;

import org.subterra.ClientSession;
import org.subterra.Entity;
import org.subterra.Main;
import org.subterra.Protocol.Packet;
import org.subterra.Protocol.PacketsHelper;
import org.subterra.Utils.ByteBuf;
import org.subterra.Utils.Vector2;

public class PlayerAuthInput extends Packet {
    private Entity entity;
    public PlayerAuthInput() { this.entity = new Entity(); }
    public PlayerAuthInput(Entity entity) {
        this.entity = entity;
    }
    @Override
    public void write(ByteBuf buffer) {
        buffer.writeString(entity.getUuid().toString());

        buffer.writeFloat(entity.getPosition().x);
        buffer.writeFloat(entity.getPosition().y);

        buffer.writeFloat(entity.getRotation());

        buffer.writeFloat(entity.getVelocity().x);
        buffer.writeFloat(entity.getVelocity().y);
    }

    @Override
    public void read(ByteBuf buffer) {
        entity.setPosition(new Vector2(buffer.readFloat(), buffer.readFloat()));
        entity.setRotation(buffer.readFloat());
        entity.setVelocity(new Vector2(buffer.readFloat(), buffer.readFloat()));
    }

    @Override
    public void process(ClientSession client) {
        client.getEntity().setRotation(this.entity.getRotation());
        client.getEntity().setVelocity(this.entity.getVelocity());
        client.getEntity().setPosition(this.entity.getPosition());

        for (ClientSession cl : Main.sessions) {
            if (!cl.equals(client)) {
                PacketsHelper.send(cl, new PlayerAuthInput(client.getEntity()));
            }
        }
    }
}
