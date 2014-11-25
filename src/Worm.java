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

    private int[] Gene;
    private int[] inheritedGene;
    private HexDirection direction;
    private int mass;
    private final int WEIGHT_LOSS_PER_ROUND = 1;

    public Worm(int x, int y, int mass) {
        super(x, y);
        this.Gene = new int[getDirectionsNumber()];
        this.inheritedGene = new int[getDirectionsNumber()];
        for(int i=0; i<getDirectionsNumber(); i++) {
            Gene[i] = getRandomGeneValue();
            inheritedGene[i] = Gene[i];
        }
        this.direction = getRandomDirection();
        this.mass = mass;
    }

    public Worm(int x, int y, Worm parent) {
        super(x, y);
        this.direction = parent.getDirection();
        this.mass = (int)(0.5*parent.getMass());
        this.inheritedGene = parent.getGenes();

        this.Gene = new int[getDirectionsNumber()];
        int[] grandpaGenes = parent.getInheritedGenes();
        int diff = 0;

        for(int i=0; i<getDirectionsNumber(); i++) {
            diff = inheritedGene[i] - grandpaGenes[i];
            Gene[i] = inheritedGene[i] + (int)(diff*getRandomPercent());
            if(Gene[i]<0)
                Gene[i] = 0;
            else if(Gene[i]>50)
                Gene[i] = 50;
        }
        mutate();
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
        int sum = 0;
        for (int j = 0; j < probability.length; j++) {
            sum += Math.exp(-1 * Gene[j]);
        }
        for (int i = 0; i < probability.length; i++) {
            probability[i] = Math.exp(-1 * Gene[i]) / sum;
        }

        /**
         * pick index based on probabilities
         */
        double probabilitiesSum = 0;
        double tmp = 0;
        for (int i = 0; i < probability.length; i++)
            probabilitiesSum += probability[i];
        double rand = Math.random() * probabilitiesSum;

        for (int i = 0; i < probability.length; i++) {
            tmp += probability[i];
            if (rand < tmp) {
                return direction = HexDirection.values()[i];
            }
        }

        return direction = HexDirection.values()[getDirectionsNumber() - 1];
    }


    public void mutate() {
        int i = getRandomDirectionIndex();
        Gene[i] += getRandomGeneMod();
        if(Gene[i]<0)
            Gene[i] = 0;
        else if(Gene[i]>50)
            Gene[i] = 50;
    }

    public int getMass() {
        return mass;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public HexDirection getDirection() {
        return direction;
    }

    public int[] getGenes() {
        return Gene;
    }

    public int[] getInheritedGenes() {
        return inheritedGene;
    }

    private HexDirection getRandomDirection() {
        return HexDirection.values()[getRandomDirectionIndex()];
    }

    private int getRandomGeneValue() {
        return (int) (Math.random() * 51);
    }

    private int getRandomGeneMod() {
        return (int) (-10 + Math.random() * 21);
    }

    private float getRandomPercent() {
        return ( (int)(-100 + Math.random() * 301) )/100;
    }

    private int getRandomDirectionIndex() {
        return (int) (Math.random() * getDirectionsNumber());
    }

    private int getDirectionsNumber() {
        return HexDirection.values().length;
    }
}
