/**
 *
 * [description lang:pl]
 Bakterie - pojawiaja sie losowo i pozostaja nieruchomo w miejscu
 dopoki nie zostana zjedzone. Nie zmieniaja sie i nie rozmnazaja z
 uplywem czasu.
 *
 * Created by Naxster on 2014-11-23.
 */
public class Bacteria extends Hex {
    private final int mass;

    public Bacteria(int x, int y, int mass) {
        super(x, y);
        this.mass = mass;
    }

    public int getEaten() {
        return mass;
    }
}
