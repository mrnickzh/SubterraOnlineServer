package org.subterra;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.subterra.Protocol.Packets.EntityAction;
import org.subterra.Protocol.PacketsHelper;
import org.subterra.World.BlockMap;
import org.subterra.World.ChunkGenerator;
import org.subterra.World.Map2D;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@ServerEndpoint("/")
public class Main {
    public static Set<ClientSession> sessions = Collections.synchronizedSet(new HashSet<>());
    public static Set<Entity> entityStorage = Collections.synchronizedSet(new HashSet<>());
    public static Map2D worldChunks = new Map2D();
    public static BlockMap blockmap = new BlockMap();
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
        JsonElement jelement;
        try {
            jelement = JsonParser.parseString(new String(Files.readAllBytes(Paths.get("blockmap.json")), StandardCharsets.UTF_8));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        JsonArray layerarray = jelement.getAsJsonArray();
        for (JsonElement layer : layerarray) {
            HashMap<Integer, Float> layermap = new HashMap<>();
            JsonObject layerobject = layer.getAsJsonObject();
            float height = layerobject.getAsJsonPrimitive("height").getAsFloat();
            JsonArray chances = layerobject.getAsJsonArray("blockmap");
            for (JsonElement block : chances) {
                JsonObject blockobject = block.getAsJsonObject();
                layermap.put(blockobject.getAsJsonPrimitive("id").getAsInt(), blockobject.getAsJsonPrimitive("chance").getAsFloat());
            }
            blockmap.append(height, layermap);
        }

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
