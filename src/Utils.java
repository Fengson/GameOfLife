/**
 * Created by Grzegorz Poręba on 2014-11-25.
 *
 * Copyright Grzegorz Poręba. All rights reserved.
 */

/**
 *
 * @author Grzegorz Poręba
 */

public class Utils {

    public static int getRandomMass() {
        return 1 + (int) (Math.random() * Constants.MAX_WORM_WEIGHT);
    }

    public static int getRandomGeneValue() {
        return (int) (Math.random() * (Constants.MAX_GENE_VALUE + 1));
    }

    public static int getRandomGeneMod() {
        return (int) (-10 + Math.random() * 21);
    }

    public static float getRandomPercent() {
        return ((int) (-100 + Math.random() * 301)) / 100;
    }

    public static HexDirection getRandomDirection() {
        return HexDirection.values()[getRandomDirectionIndex()];
    }

    public static int getRandomDirectionIndex() {
        return (int) (Math.random() * getDirectionsNumber());
    }

    public static int getDirectionsNumber() {
        return HexDirection.values().length;
    }
}
