package org.solaris.Protocol.Packets;

import org.solaris.ClientSession;
import org.solaris.Entity;
import org.solaris.Main;
import org.solaris.Protocol.Packet;
import org.solaris.Protocol.PacketsHelper;
import org.solaris.Utils.ByteBuf;

import java.util.UUID;

public class HandShakePacket extends Packet {
    private String name;
    public HandShakePacket() {}

    @Override
    public void read(ByteBuf buffer) {
        this.name = buffer.readString();
    }

    @Override
    public void process(ClientSession client) {
        client.setUsername(this.name);
        client.setUuid(UUID.randomUUID());
        PacketsHelper.send(client, new AddMapObject(1, new float[]{0, 0, 0}, new float[]{0, 0, 0}));

        PacketsHelper.send(client, new AddMapObject(0, new float[]{3, 0, 3}, new float[]{0, 0, 0}));
        PacketsHelper.send(client, new AddMapObject(0, new float[]{3, 0, 4}, new float[]{0, 0, 0}));
        PacketsHelper.send(client, new AddMapObject(0, new float[]{3, 0, 5}, new float[]{0, 0, 0}));

        Entity entity = new Entity(client.getUuid(), new float[]{0, 0, 0}, new float[]{0, 0, 0});
        client.setEntity(entity);
        for (ClientSession s : Main.sessions) {
            if (!s.equals(client)) {
                PacketsHelper.send(s, new EntityAction(EntityAction.Action.Add, entity));
                PacketsHelper.send(client, new EntityAction(EntityAction.Action.Add, s.getEntity()));
                PacketsHelper.send(client, new PlayerAuthInput(s.getEntity()));
            }
        }
    }
}
