package org.subterra;

import lombok.Getter;
import lombok.Setter;
import org.subterra.Utils.Vector2;

import java.util.UUID;

@Getter
@Setter
public class Entity {
    private UUID uuid;
    private Vector2 position;
    private float rotation;
    private Vector2 velocity;
    public Entity() {
        uuid = UUID.randomUUID();
    }
    public Entity(UUID uuid, Vector2 position, float rotation) {
        this.uuid = uuid;
        this.position = position;
        this.rotation = rotation;
    }
}
