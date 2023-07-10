package Packets;

public class PacketAddBotsTank {
    private float x, y, angle;
    private int health, id;
    private String followPlayer;


    public void setFollowPlayer(String followPlayer) {
        this.followPlayer = followPlayer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setX(float x) {
        this.x = x;
    }


    public void setY(float y) {
        this.y = y;
    }


    public void setAngle(float angle) {
        this.angle = angle;
    }


    public void setHealth(int health) {
        this.health = health;
    }
}
