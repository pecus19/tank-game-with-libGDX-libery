package com.tankgame.client.models;

import com.badlogic.gdx.math.Vector2;

public class Bullet {
    public Vector2 position = new Vector2();
    public Vector2 direction = new Vector2();
    public float speed = 500.0F;


    public Bullet(Vector2 position, Vector2 direction) {
        this.position.set(position);
        this.direction.set(direction);

    }

    public void update(float delta) {
        this.position.add(this.direction.x * delta * this.speed, this.direction.y * delta * this.speed);
    }
}
