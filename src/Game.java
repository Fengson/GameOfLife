import java.util.Random;

/**
 * Class represents all cells on level
 * <p/>
 * Created by Xv0 aka Paweł Woźnica on 2014-12-01.
 */
public class Game {

    private int levelSize;
    private Hex[][] Level;
    private Random r;


    public Game(int size) {
        this.levelSize = size;
        Level = new Hex[levelSize][levelSize];
        this.r = new Random();
        int x, y;

        r.setSeed(System.currentTimeMillis());

        for (int i = 0; i < Constants.initialWormsNumber; i++) {

            y = r.nextInt(levelSize);
            x = r.nextInt(levelSize);
            if (Level[x][y] == null) {
                Level[x][y] = new Worm(x, y);
            }

        }

        r.setSeed(System.currentTimeMillis());

        for (int i = 0; i < Constants.initialBacteryNumber; i++) {

            x = r.nextInt(levelSize);
            y = r.nextInt(levelSize);
            if (Level[x][y] == null) {
                Level[x][y] = new Bacteria(x, y);
            }
        }
    }

    public void MakeStep() {

        for (int i = 0; i < levelSize; i++) {
            for (int j = 0; j < levelSize; j++) {

                if (Level[i][j] != null && Level[i][j].is(Worm.class)) {
                    Worm thisWorm = (Worm) Level[i][j];
                    if (!thisWorm.isActivated()) {
                        continue;
                    }
                    HexDirection dir = thisWorm.getWormsNewDirection();
                    int newX = TranslateDirX(i, dir);
                    int newY = TranslateDirY(j, dir);

                    if (newX >= 0 && newX < levelSize && newY >= 0 && newY < levelSize) {
                        if (Level[newX][newY] == null || Level[newX][newY].is(Bacteria.class)) {
                            if (Level[newX][newY] != null && Level[newX][newY].is(Bacteria.class)) {
                                Bacteria thisBact = (Bacteria) Level[newX][newY];
                                thisWorm.eatBacteria(thisBact.getEaten());
                            }
                            Level[newX][newY] = thisWorm;
                            thisWorm.setX(newX);
                            thisWorm.setY(newY);
                            if (!thisWorm.looseWeight()) {
                                Level[newX][newY] = null;
                            }
                            Level[i][j] = null;
                        }
                    } else if (!thisWorm.looseWeight()) {
                        Level[i][j] = null;
                    } else {
                        thisWorm.stuckCount++;
                        if (thisWorm.stuckCount >= Constants.MAX_STUCK && thisWorm.getMass()<Constants.MIN_WORM_WEIGHT) {
                            thisWorm.forceMutation();
                            thisWorm.stuckCount = 0;
                        }
                    }

                    if (thisWorm.isOverweight()) {
                        for (int k = 0; k < 6; k++) {
                            dir = WormUtils.getRandomDirection();
                            newX = TranslateDirX(i, dir);
                            newY = TranslateDirY(j, dir);
                            if (newX >= 0 && newX < levelSize && newY >= 0 && newY < levelSize && Level[newX][newY] == null)
                                break;
                        }
                        if (newX >= 0 && newX < levelSize && newY >= 0 && newY < levelSize && Level[newX][newY] == null) {
                            Worm newborn = new Worm(newX, newY, thisWorm);
                            newborn.setActivated(false);
                            Level[newX][newY] = newborn;
                        }
                        Worm newborn = new Worm(i, j, thisWorm);
                        newborn.setActivated(false);
                        Level[i][j] = newborn;
                    }
                    thisWorm.setActivated(false);
                }
            }
        }

        for (int i = 0; i < Constants.BacteryGrow; i++) {
            r.setSeed(System.currentTimeMillis());
            int x = r.nextInt(levelSize);
            int y = r.nextInt(levelSize);
            if (Level[x][y] == null) {
                Level[x][y] = new Bacteria(x, y);
            }
        }

        for (int i = 0; i < levelSize; i++) {
            for (int j = 0; j < levelSize; j++) {
                if (Level[i][j] != null && Level[i][j].is(Worm.class)) {
                    Worm thisWorm = (Worm) Level[i][j];
                    thisWorm.setActivated(true);
                }
            }
        }
    }

    private int TranslateDirX(int x, HexDirection d) {
        if (d == null) return x;
        if (x % 2 == 1) {

            switch (d) {
                case LEFT:
                    return x - 1;
                case RIGHT:
                    return x + 1;
                case TOP_LEFT:
                    return x;
                case TOP_RIGHT:
                    return x + 1;
                case BOTTOM_LEFT:
                    return x;
                case BOTTOM_RIGHT:
                    return x + 1;
            }
        } else {
            switch (d) {
                case LEFT:
                    return x - 1;
                case RIGHT:
                    return x + 1;
                case TOP_LEFT:
                    return x - 1;
                case TOP_RIGHT:
                    return x;
                case BOTTOM_LEFT:
                    return x - 1;
                case BOTTOM_RIGHT:
                    return x;
            }
        }

        return x;
    }

    private int TranslateDirY(int y, HexDirection d) {
        if (d == null) return y;
        switch (d) {
            case LEFT:
                return y;
            case RIGHT:
                return y;
            case TOP_LEFT:
                return y - 1;
            case TOP_RIGHT:
                return y - 1;
            case BOTTOM_LEFT:
                return y + 1;
            case BOTTOM_RIGHT:
                return y + 1;
        }

        return y;
    }

    public int getFieldKind(int x, int y) {

        // 0 - Empty
        // 1 - Worm
        // 2 - Bactery

        if (this.Level[x][y] == null)
            return 0;
        if (this.Level[x][y].is(Worm.class))
            return 1;
        if (this.Level[x][y].is(Bacteria.class))
            return 2;

        return 0;
    }
}



