/**
 * Created by Grzegorz Poręba on 2014-11-25.
 *
 * Copyright Grzegorz Poręba. All rights reserved.
 */

/**
 * @author Grzegorz Poręba
 */

public class WormUtils {

    public static HexDirection getDirectionByProbabilities(double probabilitiesSum, double[] probability) {
        double temporaryProbabilitiesSum = 0;
        double rand = Math.random() * probabilitiesSum;
        for (int i = 0; i < probability.length; i++) {
            temporaryProbabilitiesSum += probability[i];
            if (rand < temporaryProbabilitiesSum) {
                return HexDirection.values()[i];
            }
        }

        return HexDirection.values()[Constants.GENE_COUNT - 1];
    }

    public static double[] calculateProbability(int[] gene) {
        double sum = 0;
        double[] probabilities = new double[Constants.GENE_COUNT];
        for (int j = 0; j < probabilities.length; j++) {
            sum += Math.exp(-1 * gene[j]);
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = Math.exp(-1 * gene[i]) / sum;
        }

        return probabilities;
    }

    public static double calculateProbabilitiesSum(double[] probabilities) {
        double probabilitiesSum = 0;
        for (double p : probabilities) {
            probabilitiesSum += p;
        }
        return probabilitiesSum;
    }

    public static void generateRandomGenes(int[] gene, int[] inheritedGene) {
        for (int i = 0; i < Constants.GENE_COUNT; i++) {
            gene[i] = WormUtils.getRandomGeneValue();
            inheritedGene[i] = gene[i];
        }
    }

    public static void mutateOneGeneRandomly(int[] gene) {
        int i = WormUtils.getRandomDirectionIndex();
        gene[i] += WormUtils.getRandomGeneModifier();
        if (gene[i] < 0) {
            gene[i] = 0;
        } else if (gene[i] > Constants.MAX_GENE_VALUE) {
            gene[i] = Constants.MAX_GENE_VALUE;
        }
    }

    public static void mutateInAncestorsDirection(int[] gene, int[] inheritedGene, int[] grandpaGenes) {
        int diff;

        for (int i = 0; i < Constants.GENE_COUNT; i++) {
            diff = inheritedGene[i] - grandpaGenes[i];
            gene[i] = inheritedGene[i] + (int) (diff * WormUtils.getRandomPercent());
            if (gene[i] < 0) {
                gene[i] = 0;
            } else if (gene[i] > Constants.MAX_GENE_VALUE) {
                gene[i] = Constants.MAX_GENE_VALUE;
            }
        }
    }

    public static int getRandomMass() {
        return Constants.MIN_WORM_WEIGHT + (int) (Math.random() * (Constants.MAX_WORM_WEIGHT - Constants.MIN_WORM_WEIGHT));
    }

    public static int getRandomGeneValue() {
        return (int) (Math.random() * (Constants.MAX_GENE_VALUE + 1));
    }

    public static int getRandomGeneModifier() {
        return (int) (-30 + Math.random() * 30);
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
