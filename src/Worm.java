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
    private HexDirection direction;
    private int mass;
    private final int WEIGHT_LOSS_PER_ROUND = 1;

    public Worm(int x, int y, int mass) {
        super(x, y);
        gene = new int[getDirectionsNumber()];
        direction = getRandomDirection();
        this.mass = mass;
    }

    /**
     * @return null when worm is dead, otherwise new direction
     */
    public HexDirection getWormsNewDirectionAndLooseWeight() {
        /**
         * decrement mass
         */

        mass -= WEIGHT_LOSS_PER_ROUND;
        if (mass <= 0) {
            return null;
        }

        /**
         * calculate probabilities
         */
        double[] probability = new double[getDirectionsNumber()];
        int sum;
        for (int i = 0; i < probability.length; i++) {
            sum = 0;
            for (int j = 0; j < probability.length; j++) {
                sum += Math.exp(-1 * gene[j]);
            }
            probability[i] = Math.exp(-1 * gene[i]) / sum;
        }

        /**
         * pick index based on probabilities
         */
        double rand = Math.random() * 100;
        double probabilitiesSum = 0;
        for (int i = 0; i < probability.length; i++) {
            probabilitiesSum += probability[i];
            if (rand < probabilitiesSum) {
                return direction = HexDirection.values()[i];
            }
        }

        return direction = HexDirection.values()[getDirectionsNumber() - 1];
    }


    public void mutate() {
        gene[getRandomDirectionIndex()] = getRandomGeneValue();
    }

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    private HexDirection getRandomDirection() {
        return HexDirection.values()[getRandomDirectionIndex()];
    }

    private int getRandomGeneValue() {
        return (int) (Math.random() * 100);
    }

    private int getRandomDirectionIndex() {
        return (int) (Math.random() * getDirectionsNumber());
    }

    private int getDirectionsNumber() {
        return HexDirection.values().length;
    }
}
