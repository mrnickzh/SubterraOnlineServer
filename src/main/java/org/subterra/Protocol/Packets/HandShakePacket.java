package org.subterra.Protocol.Packets;

import org.subterra.ClientSession;
import org.subterra.Entity;
import org.subterra.Main;
import org.subterra.Protocol.Packet;
import org.subterra.Protocol.PacketsHelper;
import org.subterra.Utils.ByteBuf;

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
