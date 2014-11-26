/**
 * Created by Grzegorz Poręba on 2014-11-25.
 *
 * Copyright Grzegorz Poręba. All rights reserved.
 */

/**
 *
 * @author Grzegorz Poręba
 */

public class WormUtils {

    public static HexDirection getDirectionByProbabilities(double rand, double[] probability) {
        double temporaryProbabilitiesSum = 0;
        for (int i = 0; i < probability.length; i++) {
            temporaryProbabilitiesSum += probability[i];
            if (rand < temporaryProbabilitiesSum) {
                return HexDirection.values()[i];
            }
        }

        return HexDirection.values()[Constants.GENE_COUNT - 1];
    }

    public static double[] calculateProbability(int[] gene){
        int sum = 0;
        double[] probabilities = new double[Constants.GENE_COUNT];
        for (int j = 0; j < probabilities.length; j++) {
            sum += Math.exp(-1 * gene[j]);
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = Math.exp(-1 * gene[i]) / sum;
        }

        return probabilities;
    }

    public static double getProbabilitiesSum(double[] probabilities) {
        double probabilitiesSum = 0;
        for (double p : probabilities) {
            probabilitiesSum += p;
        }
        return probabilitiesSum;
    }

    public static int getRandomMass() {
        return 1 + (int) (Math.random() * Constants.MAX_WORM_WEIGHT);
    }

    public static int getRandomGeneValue() {
        return (int) (Math.random() * (Constants.MAX_GENE_VALUE + 1));
    }

    public static int getRandomGeneModifier() {
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
