package server.world;

import Packets.PacketAddBotsTank;
import Packets.PacketAddPlayersTank;
import Packets.PacketDevelopers;

import java.util.*;

public class ServerWorld {
    private Map<Integer, PacketAddPlayersTank> players = new LinkedHashMap<>();
    private List<PacketAddBotsTank> enemies = new LinkedList<>();
    private int id = 5;

    public void fillEnemiesList() {
        enemies.add(PacketDevelopers.createPacketEnemyAI(300f, 0f, 0f, 100, 0, ""));
        enemies.add(PacketDevelopers.createPacketEnemyAI(100f, 900f, 0f, 100, 1, ""));
        enemies.add(PacketDevelopers.createPacketEnemyAI(900f, 900f, 0f, 100, 2, ""));
        enemies.add(PacketDevelopers.createPacketEnemyAI(900f, 100f, 0f, 100, 3, ""));
        enemies.add(PacketDevelopers.createPacketEnemyAI(100f, 100f, 0f, 100, 4, ""));
    }

    public int removeEnemy() {
        int id = 0;
        if (enemies.size() > 0) {
            id = enemies.get(0).getId();
            enemies.remove(0);
        }
        return id;
    }

    public PacketAddBotsTank addEnemyAI() {
        PacketAddBotsTank ai = PacketDevelopers.createPacketEnemyAI(100f, 100f, 0f, 100, id, "");
        id++;
        enemies.add(ai);
        return ai;
    }

    public List<PacketAddBotsTank> getEnemies() {
        return enemies;
    }

    public void addPlayer(Integer id, PacketAddPlayersTank addPlayer) {
        players.put(id, addPlayer);
    }
    public boolean containsPlayer(String playerName) {
        for (PacketAddPlayersTank player : players.values()) {
            if (player.getPlayerName().equals(playerName)) return true;
        }
        return false;
    }

    public void removeId(int id) {
        players.remove(id);
    }

    public Set<Integer> getConnectedIds() {
        return players.keySet();
    }

    public Map<Integer, PacketAddPlayersTank> getPlayers() {
        return players;
    }
}
