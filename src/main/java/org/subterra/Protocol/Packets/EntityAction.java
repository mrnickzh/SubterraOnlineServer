package org.subterra.Protocol.Packets;

import org.subterra.Entity;
import org.subterra.Protocol.Packet;
import org.subterra.Utils.ByteBuf;

public class EntityAction extends Packet {
    private Entity entity;
    Action action;
    public enum Action {
        Add,
        Remove
    }
    public EntityAction(Action action, Entity entity) {
        this.action = action;
        this.entity = entity;
    }
    @Override
    public void write(ByteBuf buffer) {
        buffer.writeInt(action.ordinal());
        buffer.writeString(entity.getUuid().toString());
    }
}
