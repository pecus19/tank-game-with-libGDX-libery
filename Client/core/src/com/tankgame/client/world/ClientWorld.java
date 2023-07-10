package com.tankgame.client.world;

import ClientConnection.ClientConnection;
import com.badlogic.gdx.utils.TimeUtils;
import com.tankgame.client.models.BotTank;
import com.tankgame.client.models.Bullet;
import com.tankgame.client.models.PlayerTank;

import java.util.*;

public class ClientWorld {
    private ClientConnection clientConnection;
    private HashMap<Integer, PlayerTank> players = new HashMap<>();
    private HashMap<Integer, BotTank> enemyAIList = new LinkedHashMap<>();
    private List<Bullet> bullets = new ArrayList();


    public HashMap<Integer, PlayerTank> getPlayers() {
        return players;
    }


    public void addPlayer(int id, PlayerTank player) {
        players.put(id, player);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }


    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void removePlayer(int id) {
        players.remove(id);
    }

    public void addEnemy(int id, BotTank enemyAI) {
        enemyAIList.put(id, enemyAI);
    }

    public void removeEnemy(int id) {
        enemyAIList.remove(id);
    }

    public HashMap<Integer, BotTank> getEnemyAIList() {
        return enemyAIList;
    }

    public Set<Integer> getEnemyAIListIds() {
        return enemyAIList.keySet();
    }


    public void setClientConnection(ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
    }
}

