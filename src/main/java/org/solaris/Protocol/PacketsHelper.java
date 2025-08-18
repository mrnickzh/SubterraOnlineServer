package org.solaris.Protocol;

import lombok.SneakyThrows;
import org.solaris.ClientSession;
import org.solaris.Protocol.Packets.AddMapObject;
import org.solaris.Protocol.Packets.EntityAction;
import org.solaris.Protocol.Packets.HandShakePacket;
import org.solaris.Protocol.Packets.PlayerAuthInput;
import org.solaris.Utils.ByteBuf;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class PacketsHelper {
    private static List<Class<? extends Packet>> packets = new ArrayList<>();
    static {
        packets.add(HandShakePacket.class);
        packets.add(AddMapObject.class);
        packets.add(EntityAction.class);
        packets.add(PlayerAuthInput.class);
    }

    @SneakyThrows
    public static void send(ClientSession session, Packet packet) {
        ByteBuf buffer = new ByteBuf(1024);
        int id = packets.indexOf(packet.getClass());
        buffer.writeInt(id);
        packet.write(buffer);
        byte[] bytes = buffer.toByteArray();
        session.getSession().getBasicRemote().sendBinary(ByteBuffer.wrap(bytes));
    }

    @SneakyThrows
    public static void read(ClientSession session, ByteBuffer readBuffer) {
        byte[] receivedData = new byte[readBuffer.remaining()];
        readBuffer.get(receivedData);

        ByteBuf buffer = new ByteBuf(receivedData.length);
        buffer.fromByteArray(receivedData);

        int id = buffer.readInt();
        Packet pkt = packets.get(id).newInstance();
        pkt.read(buffer);
        pkt.process(session);
    }
}
