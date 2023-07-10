package Packets;

public class PacketBullet {
    private float positionX;
    private float positionY;
    private float directionX;
    private float directionY;

    public PacketBullet() {
    }

    public float getPositionX() {
        return this.positionX;
    }

    public float getPositionY() {
        return this.positionY;
    }

    public float getDirectionX() {
        return this.directionX;
    }

    public float getDirectionY() {
        return this.directionY;
    }

    public void setPositionX(float x) {
        this.positionX = x;
    }

    public void setPositionY(float y) {
        this.positionY = y;
    }

    public void setDirectionX(float x) {
        this.directionX = x;
    }

    public void setDirectionY(float y) {
        this.directionY = y;
    }
}
