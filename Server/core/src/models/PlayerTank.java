package models;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Rectangle;

import java.util.Iterator;

public class PlayerTank extends TankGame {
    private final BitmapFont font = new BitmapFont();
    private static MapLayer map;
    private Rectangle res;
    private boolean alive;
    private int health;

    public PlayerTank(float x, float y, float angle, String name, MapLayer tiledMapTileLayer) {
        super(x, y, 20, angle, name, tiledMapTileLayer, "player_tank_base.png");
//        font = new BitmapFont();
        map = tiledMapTileLayer;
        this.res = new Rectangle(x, y, 32, 32);
        this.alive = true;
        this.health = 100;
    }

    @Override
    public void move() {
        super.move();
    }

    @Override
    public void render(Batch batch) {
        super.render(batch);
        this.font.draw(batch, this.name, this.position.x - 35, this.position.y + 45);
        Iterator var2 = this.bullets.iterator();
        while (var2.hasNext()) {
            Bullet bullet = (Bullet) var2.next();
            bullet.render(batch);
        }
    }
    public boolean isAlive() {
        return alive;
    }

    public static PlayerTank createPlayer(float x, float y, float angle, String name) {
        return new PlayerTank(x, y, angle, name, map);
    }


}
