/**
 * Created by Grzegorz Poręba on 2014-11-25.
 *
 * Copyright Grzegorz Poręba. All rights reserved.
 */

/**
 * [description lang:pl]
 * Robaki poruszaja sie losowo, ruch jest kontrolowany przez "geny"
 * ruchu. W kazdej chwili robak ma ustalona pozycje i orientacje.
 * Podczas kroku czasowego, kazdy robak obraca sie o losowy kat i
 * przemieszcza o jednostkowa odleglosc w wybranum kierunku. Zmiana kata
 * jest kontrolowana przez gen ktory koduje prawdopodobienstwo obrotu o
 * arbitralna wielkosc. poczatkowo zmiana o kazda wartosc kata jest
 * jednakowo prawdopodobna. Gdy robak probuje wyjsc poza plaszczyzne
 * pozostaje w miejscu..
 * <p/>
 * Jesli robak znajsdie w soim polu bakterie zjada ja i zyskuje na wadze
 * o wartosc rowna wadze bakterii. W kazdym kroku czasowym robak traci
 * ustalona wartosc ze swojej wagi,a jesli jego waga psadnie do zera
 * ginie. Jesli jego waga przekroczy pewna ustalona wartosc maksymalna
 * dzieli sie na dwa - kazdy z polowa jego wyjsciowej wagi. Kazdy z
 * nowych robakow ma zmodyfikowany w stosunku do rodzica jeden gen na
 * podstawie ktorego jest losowany jego kierunek ruchu.
 * <p/>
 * Geny kodujace obrot mozemy modelowac nastepujaco. Jesli sklada sie z
 * pol szesciokatnych kazdemu robakowi przypisujemy6 liczb calkowitych
 * wylosowanych z przedzialu (np 0-50) Dla tak wybranej planszy znaczenie
 * maja obroty o katy : ki=0,2pi/,4pi/6,pi,4pi/3,5pi/3.
 * Prawdopodobienstwo wyboru kazdego z katow dane jest jako:
 * p(ki)=exp(-gi)/suma(exp(-gi)) (suma oznacza sumowanie po elementach).
 * Podczas mutacji jedna losowo wybrana liczba gi ulega zmianie na nowa,
 * losowa wartosc.
 *
 * @author Grzegorz Poręba & Naxster
 */

public class Worm extends Hex {

    private final int[] gene;
    private final int[] inheritedGene;

    private HexDirection direction;

    private final double[] probability;
    private double probabilitiesSum;

    private int mass;

    /**
     * worm born out of ashes!
     *
     * @param x
     * @param y
     */
    public Worm(int x, int y) {
        super(x, y);
        this.direction = WormUtils.getRandomDirection();
        this.mass = WormUtils.getRandomMass();
        this.gene = new int[Constants.GENE_COUNT];
        this.inheritedGene = new int[Constants.GENE_COUNT];

        generateRandomGenes();
        this.probability = WormUtils.calculateProbability(gene);
        this.probabilitiesSum = WormUtils.getProbabilitiesSum(probability);
    }

    /**
     * hydra born worm
     *
     * @param x
     * @param y
     * @param parent
     */
    public Worm(int x, int y, Worm parent) {
        super(x, y);
        this.direction = parent.getDirection();
        this.mass = (int) (0.5 * parent.getMass());
        this.gene = new int[Constants.GENE_COUNT];
        this.inheritedGene = parent.getGenes();

        mutateInAncestorsDirection(parent.getInheritedGenes());
        mutateOneGeneRandomly();
        probability = WormUtils.calculateProbability(gene);
        probabilitiesSum = WormUtils.getProbabilitiesSum(probability);
    }

    /**
     * @return null when worm is dead, otherwise new direction
     */
    public HexDirection getWormsNewDirectionAndLooseWeight() {
        mass -= Constants.WEIGHT_LOSS_PER_ROUND;
        if (mass <= 0) {
            return null;
        }
        double rand = Math.random() * probabilitiesSum;
        return direction = WormUtils.getDirectionByProbabilities(rand, probability);
    }

    public void eatBacteria(int bacteriaMass) {
        mass += bacteriaMass;
    }

    public boolean isOverweight() {
        return mass > Constants.MAX_WORM_WEIGHT;
    }

    private void generateRandomGenes() {
        for (int i = 0; i < Constants.GENE_COUNT; i++) {
            gene[i] = WormUtils.getRandomGeneValue();
            inheritedGene[i] = gene[i];
        }
    }

    private void mutateOneGeneRandomly() {
        int i = WormUtils.getRandomDirectionIndex();
        gene[i] += WormUtils.getRandomGeneModifier();
        if (gene[i] < 0) {
            gene[i] = 0;
        } else if (gene[i] > Constants.MAX_GENE_VALUE) {
            gene[i] = Constants.MAX_GENE_VALUE;
        }
    }

    private void mutateInAncestorsDirection(int[] grandpaGenes) {
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

    public HexDirection getDirection() {
        return direction;
    }


    private int getMass() {
        return mass;
    }

    private int[] getGenes() {
        return gene;
    }

    private int[] getInheritedGenes() {
        return inheritedGene;
    }
}
