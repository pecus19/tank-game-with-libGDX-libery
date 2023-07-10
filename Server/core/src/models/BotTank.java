package models;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BotTank extends TankGame {
    private static MapLayer map;
    private int id;
    private Circle shape;



    public BotTank(float x, float y, float angle, MapLayer collisonLayer) {
        super(x, y, 20, angle, "", collisonLayer, "player_tank_base.png");
        map = collisonLayer;
        shape = new Circle();
        shape.setPosition(x, y);
        shape.setRadius(400f);
    }

    public void move(Vector2 pos) {

        double speed;
        abstractBox = new Rectangle(position.x, position.y, 32, 32);
        if (isCollision()){
            speed = 0.2;
        } else speed = 1;

        if (shape.contains(pos.x, pos.y)) {
            // for enemy to follow player
            float x = pos.x - position.x;
            float y = pos.y - position.y;
            float angle = (float) Math.atan2(y, x);
            position.x += speed * Math.cos(angle);
            position.y += speed * Math.sin(angle);
            shape.setPosition(position.x, position.y);
        }

    }

}
