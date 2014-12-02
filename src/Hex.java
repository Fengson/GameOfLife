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

    protected int mass = -1;

    public Hex() {
    }

    /**
     * @param c - class (ex. Worm.class)
     * @return true if this Hex is also instance of c Class, false otherwise
     */

    public boolean is(Class c) {
        return c.isInstance(this);
    }


    public int getMass() {
        return mass;
    }
}
