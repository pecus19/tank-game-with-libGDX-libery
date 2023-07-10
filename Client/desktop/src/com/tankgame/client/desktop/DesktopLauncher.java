package com.tankgame.client.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tankgame.client.gameInfo.GameClient;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 992;
		config.height = 800;
		GameClient gameClient = new GameClient();
		new LwjglApplication(gameClient, config);

	}
}