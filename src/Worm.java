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

    private int[] gene;
    private int[] inheritedGene;

    private HexDirection direction;

    private int mass;

    /**
     * worm born out of ashes!
     *
     * @param x
     * @param y
     */
    public Worm(int x, int y) {
        super(x, y);
        this.gene = new int[Utils.getDirectionsNumber()];
        this.inheritedGene = new int[Utils.getDirectionsNumber()];
        generateRandomGenes();
        this.direction = Utils.getRandomDirection();
        this.mass = Utils.getRandomMass();
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
        this.gene = new int[Utils.getDirectionsNumber()];
        this.direction = parent.getDirection();
        this.mass = (int) (0.5 * parent.getMass());
        this.inheritedGene = parent.getGenes();

        mutateInAncestorsDirection(parent.getInheritedGenes());
        mutateOneGeneRandomly();
    }

    public HexDirection getDirection() {
        return direction;
    }

    /**
     * @return null when worm is dead, otherwise new direction
     */
    public HexDirection getWormsNewDirectionAndLooseWeight() {

        /**
         * decrement mass
         */
        mass -= Constants.WEIGHT_LOSS_PER_ROUND;
        if (mass <= 0) {
            return null;
        }

        /**
         * calculate probabilities
         */
        double[] probability = new double[Utils.getDirectionsNumber()];
        int sum = 0;
        for (int j = 0; j < probability.length; j++) {
            sum += Math.exp(-1 * gene[j]);
        }
        for (int i = 0; i < probability.length; i++) {
            probability[i] = Math.exp(-1 * gene[i]) / sum;
        }

        /**
         * pick index based on probabilities
         */
        double probabilitiesSum = 0;
        for (double p : probability) {
            probabilitiesSum += p;
        }

        double rand = Math.random() * probabilitiesSum;
        double temporaryProbabilitiesSum = 0;
        for (int i = 0; i < probability.length; i++) {
            temporaryProbabilitiesSum += probability[i];
            if (rand < temporaryProbabilitiesSum) {
                return direction = HexDirection.values()[i];
            }
        }

        return direction = HexDirection.values()[Utils.getDirectionsNumber() - 1];
    }

    public void eatBacteria(Bacteria bacteria) {
        mass += bacteria.getMass();
    }

    public boolean isOverweight() {
        return mass > Constants.MAX_WORM_WEIGHT;
    }

    private void generateRandomGenes() {
        for (int i = 0; i < Utils.getDirectionsNumber(); i++) {
            gene[i] = Utils.getRandomGeneValue();
            inheritedGene[i] = gene[i];
        }
    }

    private void mutateOneGeneRandomly() {
        int i = Utils.getRandomDirectionIndex();
        gene[i] += Utils.getRandomGeneMod();
        if (gene[i] < 0) {
            gene[i] = 0;
        } else if (gene[i] > Constants.MAX_GENE_VALUE) {
            gene[i] = Constants.MAX_GENE_VALUE;
        }
    }

    private void mutateInAncestorsDirection(int[] grandpaGenes) {
        int diff = 0;

        for (int i = 0; i < Utils.getDirectionsNumber(); i++) {
            diff = inheritedGene[i] - grandpaGenes[i];
            gene[i] = inheritedGene[i] + (int) (diff * Utils.getRandomPercent());
            if (gene[i] < 0) {
                gene[i] = 0;
            } else if (gene[i] > Constants.MAX_GENE_VALUE) {
                gene[i] = Constants.MAX_GENE_VALUE;
            }
        }
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
