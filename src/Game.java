import java.util.Random;

/**
 * Class represents all cells on level
 * <p/>
 * Created by Xv0 aka Paweł Woźnica on 2014-12-01.
 */
public class Game {

    private int levelSize;
    private Hex[][] Level = new Hex[40][40];


    public Game(int size) {
        this.levelSize = size;

        for (int i = 0; i < levelSize; i++) {
            for (int j = 0; j < levelSize; j++) {
                Level[i][j] = null;
            }
        }

        Random r = new Random();
        int x, y;
        for (int i = 0; i < 8; i++) {
            x = r.nextInt(40);
            y = r.nextInt(40);
            if (Level[x][y] == null) {
                Level[x][y] = new Worm(x, y);
            }
        }
    }

    public void MakeStep() {

        for (int i = 0; i < levelSize; i++) {
            for (int j = 0; j < levelSize; j++) {
                if (Level[i][j] != null) {
                    if (Level[i][j].is(Worm.class)) {
                        Worm thisWorm = (Worm) Level[i][j];
                        if (thisWorm.getMass() <= 0) {
                            Level[i][j] = null;
                            return;
                        }

                        if (thisWorm.isOverweight()) {
                            Level[i][j] = new Worm(i, j, thisWorm);
                            HexDirection dir = thisWorm.getDirection();
                            int newX, newY;
                            newX = TranslateDirX(i, dir);
                            newY = TranslateDirY(i, dir);

                            if (newX >= 0 && newX <= levelSize && newY >= 0 && newY <= levelSize) {
                                if (Level[newX][newY] == null)
                                    Level[newX][newY] = new Worm(newX, newY, thisWorm);
                            }

                        } else {

                            HexDirection dir = thisWorm.getWormsNewDirectionAndLooseWeight();
                            int newX, newY;
                            newX = TranslateDirX(i, dir);
                            newY = TranslateDirY(i, dir);

                            if (newX >= 0 && newX <= levelSize && newY >= 0 && newY <= levelSize) {
                                if (Level[newX][newY] == null) {
                                    Level[newX][newY] = Level[i][j];
                                    Level[i][j] = null;
                                } else if (Level[newX][newY].is(Bacteria.class)) {
                                    Bacteria thisBact = (Bacteria) Level[newX][newY];
                                    thisWorm.eatBacteria(thisBact.getEaten());
                                    Level[newX][newY] = Level[i][j];
                                    Level[i][j] = null;
                                }
                            }


                        }
                    }

                }
            }
        }

        for (int i = 0; i < 2; i++) {
            Random r = new Random();
            int x = r.nextInt(40);
            int y = r.nextInt(40);
            if (Level[x][y] == null) {
                Level[x][y] = new Bacteria(x, y);
            }
        }
    }


    /**
     * @return return 2d array of game objects
     */
    public Hex[][] getLevel() {
        return Level;
    }

    private int TranslateDirX(int x, HexDirection d) {
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
}



