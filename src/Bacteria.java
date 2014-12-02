/**
 * [description lang:pl]
 * Bakterie - pojawiaja sie losowo i pozostaja nieruchomo w miejscu
 * dopoki nie zostana zjedzone. Nie zmieniaja sie i nie rozmnazaja z
 * uplywem czasu.
 * <p/>
 * Created by Naxster on 2014-11-23.
 */
public class Bacteria extends Hex {
    private final int mass;

    public Bacteria() {
        super();
        this.mass = getRandomMass();
    }

    /**
     * @return mass of eaten Bacteria
     */
    public int getEaten() {
        return mass;
    }

    private int getRandomMass() {
        return Constants.MIN_BACTERIA_WEIGHT + (int) (Math.random() * (Constants.MAX_BACTERIA_WEIGHT - Constants.MIN_BACTERIA_WEIGHT));
    }

}
