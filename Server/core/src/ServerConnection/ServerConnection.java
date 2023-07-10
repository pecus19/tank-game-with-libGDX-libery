package ServerConnection;

import Packets.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import models.Bullet;
import models.TankGame;
import models.PlayerTank;
import server.world.ServerWorld;

import javax.swing.*;
import java.io.IOException;
import java.util.Map;

public class ServerConnection {
    private Server server;
    private ServerWorld serverWorld = new ServerWorld();
    static final int udpPort = 8080, tcpPort = 8090;

    public ServerConnection() {
        try {
            server = new Server();
            server.start();
            server.bind(tcpPort, udpPort);

        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, "Can not start the Server.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create 5 enemies(Bots AI) on server
        serverWorld.fillEnemiesList();

        // Register all packets that the same with ClientConnection that are sent over the network.
        server.getKryo().register(PacketAddPlayersTank.class);
        server.getKryo().register(PacketDeletePlayerTanks.class);
        server.getKryo().register(PacketAddBotsTank.class);
        server.getKryo().register(PacketDeleteBotTanks.class);
        server.getKryo().register(PacketUpgradeBotTanks.class);
        server.getKryo().register(PacketDevelopers.class);
        server.getKryo().register(PacketUpgradePlayersInformation.class);
        server.getKryo().register(PlayerTank.class);
        server.getKryo().register(TankGame.class);
        server.getKryo().register(PacketBullet.class);
        server.getKryo().register(Bullet.class);


        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof PacketAddPlayersTank) {
                    // Get packet if someone connects
                    PacketAddPlayersTank connect = (PacketAddPlayersTank) object;
                    System.out.println("Connected player: " + connect.getPlayerName());
                    // Remove 1 AI if player connect and send this info to all clients
                    PacketDeleteBotTanks removeAI = new PacketDeleteBotTanks();
                    removeAI.setId(serverWorld.removeEnemy());
                    // Send to clients enemies(AI) packets
                    for (PacketAddBotsTank enemy : serverWorld.getEnemies()) {
                        server.sendToAllTCP(enemy);
                    }
                    server.sendToAllTCP(removeAI);

                    // Add player on server and send to all client packet add player
                    serverWorld.addPlayer(connection.getID(), connect);
                    for (Map.Entry<Integer, PacketAddPlayersTank> integerPacketAddPlayerEntry : serverWorld.getPlayers().entrySet()) {
                        PacketAddPlayersTank addPlayer = integerPacketAddPlayerEntry.getValue();
                        addPlayer.setId(integerPacketAddPlayerEntry.getKey());
                        server.sendToAllTCP(addPlayer);
                    }
                    System.out.println(serverWorld.getConnectedIds());
                } else if (object instanceof PacketUpgradePlayersInformation) {
                    // Get packet to update info about player and send to all clients
                    PacketUpgradePlayersInformation playerInfo = (PacketUpgradePlayersInformation) object;
                    playerInfo.setId(connection.getID());
                    server.sendToAllTCP(playerInfo);
                } else if (object instanceof PacketUpgradeBotTanks) {
                    // Get packet to update enemy(AI) info on server and send to all client this packet
                    PacketUpgradeBotTanks enemyAI = (PacketUpgradeBotTanks) object;
                    for (PacketAddBotsTank botsTank : serverWorld.getEnemies()) {
                        if (enemyAI.getId() == botsTank.getId()) {
                            botsTank.setX(enemyAI.getX());
                            botsTank.setY(enemyAI.getY());
                            botsTank.setAngle(enemyAI.getAngle());
                            botsTank.setFollowPlayer(enemyAI.getFollowPlayer());
                            botsTank.setHealth(enemyAI.getHealth());
                        }
                        // Create packet with updated info about enemy(AI)
                        PacketUpgradeBotTanks ai = PacketDevelopers.createPacketUpdateEnemyAI(
                                enemyAI.getX(), enemyAI.getY(), enemyAI.getAngle(), enemyAI.getHealth(), enemyAI.getId(), enemyAI.getFollowPlayer());
                        // Check if player not in list, enemy(AI) enough not follow it
                        if (!serverWorld.containsPlayer(enemyAI.getFollowPlayer()))
                            ai.setFollowPlayer("");
                        server.sendToAllTCP(ai);
                    }
                } else if (object instanceof PacketBullet) {
                    PacketBullet packetBullet = (PacketBullet) object;
                    server.sendToAllUDP(packetBullet);
                }
            }

            @Override
            public void disconnected(Connection connection) {
                // Get packet to remove player, and instead add enemy
                PacketDeletePlayerTanks removePlayer = PacketDevelopers.createPacketRemovePlayer(connection.getID());
                serverWorld.removeId(connection.getID());
                System.out.println("Player disconnected: " + connection.getID());
                server.sendToAllTCP(serverWorld.addEnemyAI());
                server.sendToAllTCP(removePlayer);
            }
        });
    }

    public static void main(String[] args) {
        new ServerConnection();
    }

}
