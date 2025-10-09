package org.subterra.Protocol.Packets;

import org.subterra.ClientSession;
import org.subterra.Entity;
import org.subterra.Main;
import org.subterra.Protocol.Packet;
import org.subterra.Protocol.PacketsHelper;
import org.subterra.Utils.ByteBuf;

public class PlayerAuthInput extends Packet {
    private Entity entity;
    public PlayerAuthInput() { this.entity = new Entity(); }
    public PlayerAuthInput(Entity entity) {
        this.entity = entity;
    }
    @Override
    public void write(ByteBuf buffer) {
        buffer.writeString(entity.getUuid().toString());

        buffer.writeFloat(entity.getPosition()[0]);
        buffer.writeFloat(entity.getPosition()[1]);
        buffer.writeFloat(entity.getPosition()[2]);

        buffer.writeFloat(entity.getRotation()[0]);
        buffer.writeFloat(entity.getRotation()[1]);
        buffer.writeFloat(entity.getRotation()[2]);

        buffer.writeFloat(entity.getVelocity()[0]);
        buffer.writeFloat(entity.getVelocity()[1]);
        buffer.writeFloat(entity.getVelocity()[2]);
    }

    @Override
    public void read(ByteBuf buffer) {
        entity.setPosition(new float[]{buffer.readFloat(), buffer.readFloat(), buffer.readFloat()});
        entity.setRotation(new float[]{buffer.readFloat(), buffer.readFloat(), buffer.readFloat()});
        entity.setVelocity(new float[]{buffer.readFloat(), buffer.readFloat(), buffer.readFloat()});
        System.out.println(String.format("%f, %f, %f", this.entity.getVelocity()[0], this.entity.getVelocity()[1], this.entity.getVelocity()[2]));
    }

    @Override
    public void process(ClientSession client) {
        client.getEntity().setRotation(this.entity.getRotation().clone());
        client.getEntity().setVelocity(this.entity.getVelocity().clone());
        client.getEntity().setPosition(this.entity.getPosition().clone());

        for (ClientSession cl : Main.sessions) {
            if (!cl.equals(client)) {
                System.out.println(String.format("PAI processed %s", client.getEntity().getUuid()));
                System.out.println(String.format("%f, %f, %f", client.getEntity().getVelocity()[0], client.getEntity().getVelocity()[1], client.getEntity().getVelocity()[2]));
                PacketsHelper.send(cl, new PlayerAuthInput(client.getEntity()));
            }
        }
    }
}
