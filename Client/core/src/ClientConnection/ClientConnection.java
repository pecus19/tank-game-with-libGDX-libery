package ClientConnection;

import Packets.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.tankgame.client.gameInfo.GameClient;
import com.tankgame.client.models.BotTank;
import com.tankgame.client.models.Bullet;
import com.tankgame.client.models.TankGame;
import com.tankgame.client.models.PlayerTank;
import com.tankgame.client.screens.GameScreen;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.tankgame.client.world.ClientWorld;

import javax.swing.*;
import java.io.IOException;

public class ClientConnection {
    private Client client;
    private String playerName;
    private GameScreen gameScreen;
    private GameClient gameClient;
    private ClientWorld clientWorld;

    public ClientConnection() {
        String ip = "localhost";
//        String ip = "193.40.156.227"; // TalTech Server

        int udpPort = 8080, tcpPort = 8090;

        client = new Client();
        client.start();

        // Register all packets that are sent over the network.
        client.getKryo().register(PacketAddPlayerTank.class);
        client.getKryo().register(PacketDeletePlayerTank.class);
        client.getKryo().register(PacketAddBotTank.class);
        client.getKryo().register(PacketDeleteBotTank.class);
        client.getKryo().register(PacketUpgradeBotTank.class);
        client.getKryo().register(PacketDeveloper.class);
        client.getKryo().register(PacketUpgradePlayerInformation.class);
        client.getKryo().register(PlayerTank.class);
        client.getKryo().register(TankGame.class);
        client.getKryo().register(PacketBullet.class);

        client.addListener(new Listener() {
            @Override
            public void received(final Connection connection, final Object object) {
                if (object instanceof PacketAddPlayerTank) {
                    // Get packet to add player and create thread(because of texture) to create player
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            PacketAddPlayerTank addPlayer = (PacketAddPlayerTank) object;

                            // Check if client world not contains and packet if not equal current id
                            if (addPlayer.getId() != connection.getID() || clientWorld.getPlayers().containsKey(addPlayer.getId())) {
                                final PlayerTank player = PlayerTank.createPlayer(200f, 300f, 0f, addPlayer.getPlayerName());
                                clientWorld.addPlayer(addPlayer.getId(), player);
                            }
                            System.out.println(addPlayer.getPlayerName() + " connected!");

                        }
                    });
                } else if (object instanceof PacketUpgradePlayerInformation) {
                    // Get packet to update player if client world contains it
                    final PacketUpgradePlayerInformation playerInfo = (PacketUpgradePlayerInformation) object;
                    if (clientWorld.getPlayers().containsKey(playerInfo.getId())) {
                        PlayerTank newPlayer = clientWorld.getPlayers().get(playerInfo.getId());
                        newPlayer.setPosition(playerInfo.getX(), playerInfo.getY());
                        newPlayer.setAngle(playerInfo.getAngle());
                        newPlayer.setDirection(playerInfo.getDirection());
                    }

                } else if (object instanceof PacketDeletePlayerTank) {
                    // Get packet to remove player
                    PacketDeletePlayerTank removePlayer = (PacketDeletePlayerTank) object;
                    System.out.println("Player disconnected: " + connection.getID());
                    clientWorld.removePlayer(removePlayer.getId());

                } else if (object instanceof PacketAddBotTank) {
                    // Get packet to add botTank and create thread to it
                    final PacketAddBotTank botTank = (PacketAddBotTank) object;
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            final BotTank enemyBot = BotTank.createEnemyAI(botTank.getX(), botTank.getY(), botTank.getAngle(), botTank.getHealth(), botTank.getFollowPlayer());
                            enemyBot.setId(botTank.getId());
                            clientWorld.addEnemy(botTank.getId(), enemyBot);
                        }
                    });

                } else if (object instanceof PacketUpgradeBotTank) {
                    // Get packet to update botTank info
                    PacketUpgradeBotTank botTank = (PacketUpgradeBotTank) object;
                    if (clientWorld.getEnemyAIList().containsKey(botTank.getId())) {
                        BotTank ai = clientWorld.getEnemyAIList().get(botTank.getId());
                        ai.setPosition(botTank.getX(), botTank.getY());
                        ai.setAngle(botTank.getAngle());
                        ai.setFollowPlayer(botTank.getFollowPlayer());
                    }

                } else if (object instanceof PacketDeleteBotTank) {
                    // Get packet to remove botTank
                    PacketDeleteBotTank botTank = (PacketDeleteBotTank) object;
                    if (clientWorld.getEnemyAIListIds().contains(botTank.getId())) {
                        clientWorld.removeEnemy(botTank.getId());
                    }
                } else if (object instanceof PacketBullet) {
                    PacketBullet packetBullet = (PacketBullet) object;
                    Bullet bullet = new Bullet(new Vector2(packetBullet.getPositionX(), packetBullet.getPositionY()),
                            new Vector2(packetBullet.getDirectionX(), packetBullet.getDirectionY()));
                    clientWorld.addBullet(bullet);
                }
            }
        });

        try {
            // Connected to the server - wait 5000ms before failing.
            client.connect(5000, ip, tcpPort, udpPort);
        } catch (IOException exception) {
            JOptionPane.showMessageDialog(null, "Can not connect to the Server!");
        }
    }

    public void updatePlayer(float x, float y, float angle, String direction, int health) {
        PacketUpgradePlayerInformation updatePlayerInfo = PacketDeveloper.createPacketUpdatePlayer(x, y, angle, direction, health, client.getID());
        client.sendTCP(updatePlayerInfo);
    }

    public void addBullet(float posX, float posY, float dirX, float dirY) {
        PacketBullet packetBullet = PacketDeveloper.createPacketBullet(posX, posY, dirX, dirY);
        client.sendUDP(packetBullet);
    }

    public void updateBotTank(float x, float y, float angle, int health, int id, String follow) {
        PacketUpgradeBotTank addEnemyAI = PacketDeveloper.createPacketUpdateEnemyAI(x, y, angle, health, id, follow);
        client.sendTCP(addEnemyAI);
    }

    public void sendPacketConnect() {
        PacketAddPlayerTank packetConnect = PacketDeveloper.createPacketAddPlayer(playerName);
        client.sendTCP(packetConnect);
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public GameClient getGameClient() {
        return gameClient;
    }

    public void setGameClient(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ClientWorld getClientWorld() {
        return clientWorld;
    }

    public void setClientWorld(ClientWorld clientWorld) {
        this.clientWorld = clientWorld;
    }

    public static void main(String[] args) {
        new ClientConnection();
    }
}
