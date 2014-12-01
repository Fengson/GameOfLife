/**
 * Created by Grzegorz Poręba on 2014-11-25.
 *
 * Copyright Grzegorz Poręba. All rights reserved.
 */

/**
 * Class representing single hex field
 *
 * @author Grzegorz Poręba
 */

public class Hex {

    private int x;
    private int y;

    public Hex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * @param c - class (ex. Worm.class)
     * @return true if this Hex is also instance of c Class, false otherwise
     */

    public boolean is(Class c) {
        return c.isInstance(this);
    }


}
