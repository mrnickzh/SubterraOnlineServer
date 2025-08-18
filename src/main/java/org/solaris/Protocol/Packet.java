package org.solaris.Protocol;

import org.solaris.ClientSession;
import org.solaris.Utils.ByteBuf;

public class Packet {
    public void read(ByteBuf buffer) {}
    public void write(ByteBuf buffer) {}
    public void process(ClientSession client) {}
}
