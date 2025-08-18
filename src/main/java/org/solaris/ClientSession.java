package org.solaris;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.websocket.Session;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public class ClientSession {
    private Session session;
    private String username;
    private UUID uuid;
    private Entity entity;

    ClientSession(Session session) {
        this.session = session;
    }
}
