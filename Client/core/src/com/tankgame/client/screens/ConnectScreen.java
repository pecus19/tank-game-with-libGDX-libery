package com.tankgame.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tankgame.client.gameInfo.GameClient;

import javax.swing.*;

public class ConnectScreen implements Screen {
    private final Stage stage;
    private final TextField username;
    private final TextField health;
    private final TextButton connectButton;
    private final TextButton quitButton;
    private GameClient gameClient;

    private final Table root;

    public ConnectScreen() {

        final Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        stage = new Stage();
        root = new Table();
        root.setBounds(0, 0, 1000, 800);
        username = new TextField("Player Name : ", skin);
        health = new TextField("Health: 100", skin);
        connectButton = new TextButton("Connect to the game!", skin);
        quitButton = new TextButton("Exit", skin);

        listenButtons();

        stage.addActor(this.root);

        root.add(username).width(700).padTop(0).row();
//        root.add(health).width(700).padTop(0).row();
        root.add(connectButton).size(400, 50).padTop(50).row();
        root.add(quitButton).size(400, 50).padTop(10).row();

    }

    public void listenButtons() {
        connectButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                String name = username.getText();
                if (name.replaceAll("\\s", "").equals("") || name.equals("Username")) {
                    JOptionPane.showMessageDialog(null, "Please write your player name: ");
                    return false;
                } else {
                    gameClient.startGame(name);
                    return super.touchDown(event, x, y, pointer, button);
                }
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act(delta);
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
    public void dispose() {

    }


    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
    }
}
