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

    public Hex() {
    }

    /**
     * @param c - class (ex. Worm.class)
     * @return true if this Hex is also instance of c Class, false otherwise
     */

    public boolean is(Class c) {
        return c.isInstance(this);
    }

    public int hexMass(Hex currentHex) {

        if(currentHex.is(Worm.class)){
            return ((Worm)currentHex).getMass();
        }

        if(currentHex.is(Bacteria.class)){
            return ((Bacteria)currentHex).getEaten();
        }

        return -1;

    }
}
