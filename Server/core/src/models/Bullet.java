package models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    public Vector2 position = new Vector2();
    public Vector2 direction = new Vector2();
    public float speed = 350.0F;
    public Texture texture;

    public Bullet(Vector2 position, Vector2 direction) {
        this.position.set(position);
        this.direction.set(direction);
        if (this.texture == null) {
            this.texture = new Texture("projectile.png");
        }

    }

    public void update(float delta) {
        this.position.add(this.direction.x * delta * this.speed, this.direction.y * delta * this.speed);
    }

    public void render(Batch batch) {
        batch.draw(this.texture, this.position.x, this.position.y);
    }
}
