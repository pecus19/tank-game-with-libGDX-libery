package Packets;

public class PacketDeveloper {

    public PacketDeveloper() {
    }

    public static PacketAddPlayerTank createPacketAddPlayer(String name) {
        PacketAddPlayerTank packetConnect = new PacketAddPlayerTank();
        packetConnect.setPlayerName(name);
        return packetConnect;
    }

    public static PacketUpgradePlayerInformation createPacketUpdatePlayer(float x, float y, float angle, String direction, int health, int id) {
        PacketUpgradePlayerInformation packetPlayerInfo = new PacketUpgradePlayerInformation();
        packetPlayerInfo.setX(x);
        packetPlayerInfo.setY(y);
        packetPlayerInfo.setAngle(angle);
        packetPlayerInfo.setDirection(direction);
        packetPlayerInfo.setHealth(health);
        packetPlayerInfo.setId(id);
        return packetPlayerInfo;
    }

    public static PacketBullet createPacketBullet(float posX, float posY, float dirX, float dirY) {
        PacketBullet packetBullet = new PacketBullet();
        packetBullet.setDirectionX(dirX);
        packetBullet.setDirectionY(dirY);
        packetBullet.setPositionX(posX);
        packetBullet.setPositionY(posY);
        return packetBullet;
    }


    public static PacketUpgradeBotTank createPacketUpdateEnemyAI(float x, float y, float angle, int health, int id, String follow) {
        PacketUpgradeBotTank packetAddEnemyAI = new PacketUpgradeBotTank();
        packetAddEnemyAI.setX(x);
        packetAddEnemyAI.setY(y);
        packetAddEnemyAI.setAngle(angle);
        packetAddEnemyAI.setHealth(health);
        packetAddEnemyAI.setId(id);
        packetAddEnemyAI.setFollowPlayer(follow);
        return packetAddEnemyAI;
    }
}
