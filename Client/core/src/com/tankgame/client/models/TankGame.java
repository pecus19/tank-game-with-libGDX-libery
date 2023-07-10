package com.tankgame.client.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tankgame.client.world.ClientWorld;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;


import java.util.ArrayList;


public class TankGame {
    public ClientWorld world = new ClientWorld();
    public final Vector2 position;
    private Texture picture;
    private TextureRegion pictureRegion;
    public Vector2 dir;
    public Vector2 origin;
    public Vector2 offset;
    public Vector2 barrelPosition;
    public Vector2 barrelOffset;
    private Vector2 directionv = new Vector2();


    public TiledMap map;

    public String name;
    public int health;
    private final float size, halfSize;
    public float angle;
    public String direction = "up";
    public Weapon weapon = new Weapon();

    public float oldX;
    public float oldY;
    public Texture pictureHp;
    public TextureRegion pictureRegionHp;

    public boolean flagLeft = false;
    public boolean flagRight = false;
    public boolean flagUp = false;
    public boolean flagDown = false;
    public MapLayer collisionLayer;

    private Music music;


    public ArrayList<Bullet> bullets;

    public Rectangle abstractBox; //abstract rectangle that collides with map

    public TankGame(float x, float y, float size, float angle, String name, MapLayer collisionLayer, String textureName, String hp) {
        this.position = new Vector2();
        this.position.set(x, y);
        this.name = name;
        this.size = size;
        this.angle = angle;
        this.health = 100;
        this.halfSize = size / 2;
        this.picture = new Texture(textureName);
        map = new TmxMapLoader().load("finalMap.tmx");
        this.pictureHp = new Texture(hp);
        this.pictureRegion = new TextureRegion(picture);
        this.pictureRegionHp = new TextureRegion(pictureHp);
        this.collisionLayer = collisionLayer; //get layer with collidable objects
        dir = (new Vector2(1.0f, 0.0f)).rotateDeg(angle);
        origin = new Vector2(0.5f, 0.5f); // rotation origin, rotate around the center of the image. ( 0,0 would have been upper left corner)
        barrelOffset = new Vector2(1.0f, -0.5f).scl(0.5f);


        offset = (new Vector2(barrelOffset)).rotateDeg(angle).add(origin); // Rotated barrel offset
        barrelPosition = (new Vector2(position)).add(offset);
        bullets = new ArrayList<>();
        Music music = Gdx.audio.newMusic(Gdx.files.internal("muzyka-dlya-nagiba-----world-of-tanks.mp3"));

        music.setVolume(0.3f);
        music.setLooping(true);
        music.play();

    }


    public void render(Batch batch) {
        batch.draw(pictureRegion,
                position.x - halfSize,
                position.y - halfSize,
                halfSize, halfSize, size, size,
                1, 1, angle);
        directionv = (new Vector2(1.0f, 0.0f)).rotateDeg(angle); // unit vector of the direction of the player
        origin = new Vector2(0.5f, 0.5f); // rotation origin, rotate around the center of the image. ( 0,0 would have been upper left corner)
        offset = (new Vector2(barrelOffset)).rotateDeg(angle).add(origin); // Rotated barrel offset
        barrelPosition = (new Vector2(position)).add(offset);
    }

    public void render2(Batch batch) {
        batch.draw(pictureRegionHp,
                position.x - halfSize,
                position.y - size,
                halfSize + 2, size + 2, halfSize + 2,
                halfSize,
                1, 1, angle);

    }

    public void move() {
        oldX = position.x;
        oldY = position.y;
        abstractBox = new Rectangle(position.x, position.y, 16, 16);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            position.x -= 2;
            this.direction = "left";
            flagLeft = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            position.x += 2;
            this.direction = "right";
            flagRight = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.y += 2;
            this.direction = "up";
            flagUp = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.y -= 2;
            this.direction = "down";
            flagDown = true;

        } else if (Gdx.input.isKeyPressed(Input.Keys.P)) {


        }
        if (isCollision()) {
            if (flagLeft) {
                flagLeft = false;
                position.x += 9;
            } else if (flagRight) {
                flagRight = false;
                position.x -= 9;
            } else if (flagUp) {
                flagUp = false;
                position.y -= 9;
            } else if (flagDown) {
                flagDown = false;
                position.y += 9;
            }
        }


    }

    public Bullet bulletCreation(Vector2 mousePos) {
        float delta = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            return new Bullet(barrelPosition, mousePos);
        }
        return null;
    }

    public boolean isCollision() {
        MapObjects frameObjects = this.map.getLayers().get("frame").getObjects();
        MapObjects notdestroyBricksObjects = this.map.getLayers().get("notdestroyBricks").getObjects();
        MapObjects destroyBricksObjects = this.map.getLayers().get("destroyBricks").getObjects();
        for (RectangleMapObject rectangleObject : frameObjects.getByType(RectangleMapObject.class)) { //get rectangle objects
            Rectangle rectangle = rectangleObject.getRectangle();
            if (rectangle.overlaps(abstractBox)) {
                return true;
            }
        }
        for (RectangleMapObject rectangleObject : notdestroyBricksObjects.getByType(RectangleMapObject.class)) { //get rectangle objects
            Rectangle rectangle = rectangleObject.getRectangle();
            if (rectangle.overlaps(abstractBox)) {
                return true;
            }
        }
        for (RectangleMapObject rectangleObject : destroyBricksObjects.getByType(RectangleMapObject.class)) { //get rectangle objects
            Rectangle rectangle = rectangleObject.getRectangle();
            if (rectangle.overlaps(abstractBox)) {
                return true;
            }
        }
        return false;
    }


    public void rotate(Vector3 mousePos) {
        angle = MathUtils.radiansToDegrees * MathUtils.atan2(mousePos.y - position.y, mousePos.x - position.x);
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setWorld(ClientWorld world) {
        this.world = world;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeapon(Weapon w) {
        this.weapon = w;
    }


    public Vector2 getPosition() {
        return position;
    }


}
