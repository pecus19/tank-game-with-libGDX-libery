package com.tankgame.client.screens;

import ClientConnection.ClientConnection;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.tankgame.client.models.BotTank;
import com.tankgame.client.models.Bullet;
import com.tankgame.client.models.PlayerTank;
import com.tankgame.client.models.Weapon;
import com.tankgame.client.world.ClientWorld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class GameScreen implements Screen, InputProcessor {
    private PlayerTank player;
    private ClientWorld world;
    private ClientConnection clientConnection;
    private OrthographicCamera camera;
    SpriteBatch batch;
    private TextureRegion backgroundTexture;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    ShapeRenderer shapeRenderer;
    private OrthogonalTiledMapRenderer tmr;


    public GameScreen(ClientWorld world) {
        this.world = world;
        backgroundTexture = new TextureRegion(new Texture("jpng.png"), 0, 0, 1920, 1080);
        batch = new SpriteBatch();
        map = new TmxMapLoader().load("finalMap.tmx");
        tmr = new OrthogonalTiledMapRenderer(map, 1);

        player = new PlayerTank(100f, 100f, 0f, "player", map.getLayers().get("Objects"));
        player.setWeapon(new Weapon());
        player.setWorld(world);

        camera = new OrthographicCamera(1000, 800);
        shapeRenderer = new ShapeRenderer();

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);

        // update player, player rotate and move

        player.move();
        player.rotate(getMousePosInGameWorld());
//        this.player.shoot();
        player.bulletCreation(new Vector2(getMousePosInGameWorld().x - player.getPosition().x, getMousePosInGameWorld().y - player.getPosition().y).nor());
        // rotate and move enemy to player
        moveAndRotateEnemiesAI();

        // update camera(follow player) and set it to batch
        camera.position.set(496, 400, 0);
        camera.update();
        updateBullets();
        batch.setProjectionMatrix(camera.combined);
        tmr.setView(camera);
        tmr.render();

        batch.begin();

        player.render(batch);
        player.render2(batch);

        drawEnemiesAI();
        drawOtherPlayers();

        batch.end();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.GOLD);
        drawBullets(shapeRenderer);
        shapeRenderer.end();


    }

    public void updateBullets() {
        List<Bullet> bullets = new ArrayList<>(world.getBullets());
        for (Bullet b : bullets) {
            b.update(Gdx.graphics.getDeltaTime());
        }
    }
    public void drawBullets(ShapeRenderer shapeRenderer) {
        List<Bullet> bullets = new ArrayList<>(world.getBullets());
        for (Bullet b : bullets) {
            shapeRenderer.circle(b.position.x, b.position.y, 03f, 32);
        }
    }

    public Vector3 getMousePosInGameWorld() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    public void drawOtherPlayers() {
        List<PlayerTank> players = new ArrayList(this.world.getPlayers().values());
        Iterator var2 = players.iterator();

        while (var2.hasNext()) {
            PlayerTank player1 = (PlayerTank) var2.next();
            player1.render(this.batch);
        }

    }

    public void moveAndRotateEnemiesAI() {
        Collection<BotTank> enemyAIS = world.getEnemyAIList().values();
        for (BotTank enemyAI : enemyAIS) {
            enemyAI.move(player);
            clientConnection.updateBotTank(enemyAI.getPosition().x, enemyAI.getPosition().y, enemyAI.angle, enemyAI.health, enemyAI.getId(), enemyAI.getFollowPlayer());
        }
    }

    public void drawEnemiesAI() {
        Collection<BotTank> enemyAIS = this.world.getEnemyAIList().values();
        Iterator var2 = enemyAIS.iterator();

        while (var2.hasNext()) {
            BotTank enemyAI = (BotTank) var2.next();
            enemyAI.render(this.batch);
            enemyAI.render2(this.batch);
        }

    }

    public PlayerTank getPlayer() {
        return player;
    }

    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        player.setClientConnection(this.clientConnection);
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose () {
        batch.dispose();
        tmr.dispose();
        map.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
