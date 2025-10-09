package org.subterra.Protocol;

import org.subterra.ClientSession;
import org.subterra.Utils.ByteBuf;

public class Packet {
    public void read(ByteBuf buffer) {}
    public void write(ByteBuf buffer) {}
    public void process(ClientSession client) {}
}
