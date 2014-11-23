/**
 * Created by Naxster on 2014-11-23.
 */
public class Bakteria {
    private int x;
    private int y;
    private int mass;

    public Bakteria(int x, int y, int mass) {
        this.x = x;
        this.y = y;
        this.mass = mass;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getEaten() {
        int masa = mass;
        this.mass = 0;
        return masa;
    }
}
