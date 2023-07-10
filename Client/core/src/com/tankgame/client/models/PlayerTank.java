package com.tankgame.client.models;

import ClientConnection.ClientConnection;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Vector2;

public class PlayerTank extends TankGame {
    private ClientConnection clientConnection;
    private final BitmapFont font;
    private static MapLayer map;

    public PlayerTank(float x, float y, float angle, String name, MapLayer tiledMapTileLayer) {
        super(x, y, 20, angle, name, tiledMapTileLayer, "player_tank_base.png", "bar.png");
        font = new BitmapFont();
        map = tiledMapTileLayer;


    }

    @Override
    public void move() {
        super.move();
        clientConnection.updatePlayer(position.x, position.y, angle, direction, health);
    }


    public void render(Batch batch) {
        super.render(batch);
        font.draw(batch, name, position.x - 35.0F, position.y + 45);
    }

    public void render2(Batch batch) {
        super.render2(batch);
        this.font.draw(batch, this.name, this.position.x - 35.0F, this.position.y + 45.0F);
    }

    @Override
    public Bullet bulletCreation(Vector2 mousePos) {
        Bullet bullet = super.bulletCreation(mousePos);
        if (bullet != null) {
            clientConnection.addBullet(bullet.position.x, bullet.position.y, bullet.direction.x, bullet.direction.y);
        }
        return bullet;
    }

    public static PlayerTank createPlayer(float x, float y, float angle, String name) {
        return new PlayerTank(x, y, angle, name, map);
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }
}
