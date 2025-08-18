package org.solaris;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Entity {
    private UUID uuid;
    private float[] position;
    private float[] rotation;
    private float[] velocity;
    public Entity() {
        uuid = UUID.randomUUID();
    }
    public Entity(UUID uuid, float[] position, float[] rotation) {
        this.uuid = uuid;
        this.position = position;
        this.rotation = rotation;
    }
}
