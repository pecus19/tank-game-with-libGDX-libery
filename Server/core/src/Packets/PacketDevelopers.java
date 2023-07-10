package Packets;

public class PacketDevelopers {

    public static PacketDeletePlayerTanks createPacketRemovePlayer(int id) {
        PacketDeletePlayerTanks removePlayer = new PacketDeletePlayerTanks();
        removePlayer.setId(id);
        return removePlayer;
    }

    public static PacketAddBotsTank createPacketEnemyAI(float x, float y, float angle, int health, int id, String follow) {
        PacketAddBotsTank packetAddEnemyAI = new PacketAddBotsTank();
        packetAddEnemyAI.setX(x);
        packetAddEnemyAI.setY(y);
        packetAddEnemyAI.setAngle(angle);
        packetAddEnemyAI.setHealth(health);
        packetAddEnemyAI.setId(id);
        packetAddEnemyAI.setFollowPlayer(follow);
        return packetAddEnemyAI;
    }

    public static PacketUpgradeBotTanks createPacketUpdateEnemyAI(float x, float y, float angle, int health, int id, String follow) {
        PacketUpgradeBotTanks packetAddEnemyAI = new PacketUpgradeBotTanks();
        packetAddEnemyAI.setX(x);
        packetAddEnemyAI.setY(y);
        packetAddEnemyAI.setAngle(angle);
        packetAddEnemyAI.setHealth(health);
        packetAddEnemyAI.setId(id);
        packetAddEnemyAI.setFollowPlayer(follow);
        return packetAddEnemyAI;
    }

}
