/**
 * Created by Grzegorz Poręba on 2014-11-25.
 *
 * Copyright Grzegorz Poręba. All rights reserved.
 */

/**
 *
 * @author Grzegorz Poręba
 */

public class Constants {

    public static final int LEVEL_SIZE = 40;

    public static final int MAX_GENE_VALUE = 50;
    public static final int WEIGHT_LOSS_PER_ROUND = 1;
    public static final int MAX_WORM_WEIGHT = 100;
    public static final int MIN_WORM_WEIGHT = 60;
    public static final int MAX_BACTERIA_WEIGHT = 40;
    public static final int MIN_BACTERIA_WEIGHT = 20;
    public static final int MAX_STUCK = 5;
    public static final int GENE_COUNT = HexDirection.values().length;
    public static final int INITIAL_WORMS_NUMBER = 20;
    public static final int INITIAL_BACTERIA_NUMBER = 400;
    public static final int BACTERIA_GROWTH = 3000;

    public static int turnLengthInMs;
    public static boolean isRunning = false;
    public static int nextStep = 0;

}
