package org.subterra;

import org.subterra.Protocol.Packets.EntityAction;
import org.subterra.Protocol.PacketsHelper;
import org.subterra.World.ChunkGenerator;
import org.subterra.World.Map2D;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/")
public class Main {
    public static Set<ClientSession> sessions = Collections.synchronizedSet(new HashSet<>());
    public static Set<Entity> entityStorage = Collections.synchronizedSet(new HashSet<>());
    public static Map2D worldChunks = new Map2D();
    public static ChunkGenerator chunkgen = new ChunkGenerator();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(new ClientSession(session));
        System.out.println("Connected: " + session.getId());
    }

    @OnMessage
    public void onBinaryMessage(ByteBuffer message, Session session) {
        ClientSession cl = sessions.stream().filter(c -> c.getSession().equals(session)).findAny().orElse(null);
        PacketsHelper.read(cl, message);
    }

    @OnClose
    public void onClose(Session session) {
        ClientSession cl = sessions.stream().filter(c -> c.getSession().equals(session)).findAny().orElse(null);
        if(cl != null) {
            for (ClientSession c : sessions) {
                if(!c.equals(cl)) {
                    PacketsHelper.send(cl, new EntityAction(EntityAction.Action.Remove, cl.getEntity()));
                }
            }
        }
        sessions.removeIf(cs -> cs.getSession().equals(session));
        System.out.println("Closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error for " + session.getId());
        throwable.printStackTrace();
    }

    public static void main(String[] args) {
        org.glassfish.tyrus.server.Server server =
                new org.glassfish.tyrus.server.Server("localhost", 30303, "/", null, Main.class);

        try {
            server.start();
            while (true) {}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
