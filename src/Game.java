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
        spawnWormsOnInit();
        spawnBacteriasOnInit();
    }

    private void spawnBacteriasOnInit() {
        r.setSeed(System.currentTimeMillis());
        int x,y;
        for (int i = 0; i < Constants.INITIAL_BACTERIA_NUMBER; i++) {
            x = r.nextInt(levelSize);
            y = r.nextInt(levelSize);
            if (Level[x][y] == null) {
                Level[x][y] = new Bacteria();
            }
        }
    }

    private void spawnWormsOnInit() {
        r.setSeed(System.currentTimeMillis());
        int y,x;
        for (int i = 0; i < Constants.INITIAL_WORMS_NUMBER; i++) {
            y = r.nextInt(levelSize);
            x = r.nextInt(levelSize);
            if (Level[x][y] == null) {
                Level[x][y] = new Worm();
            }

        }
    }

    public void makeStep() {
        for (int i = 0; i < levelSize; i++) {
            for (int j = 0; j < levelSize; j++) {
                if (Level[i][j] != null && Level[i][j].is(Worm.class)) {
                    makeWormTurn(i, j);
                }
            }
        }
        spawnRandomBacteries();
        resetWormsChecked();
    }

    private void makeWormTurn(int i, int j) {
        Worm thisWorm = (Worm) Level[i][j];
        if (thisWorm.isChecked()) {
            return;
        }
        HexDirection dir = thisWorm.getWormsNewDirection();
        int newX = TranslateDirection(i, j, dir, 'x');
        int newY = TranslateDirection(i, j, dir, 'y');

        if (newX >= 0 && newX < levelSize && newY >= 0 && newY < levelSize && (Level[newX][newY] == null || Level[newX][newY].is(Bacteria.class))) {
            moveWormAndEatBacteriaIfAble(thisWorm, newX, newY);
            //worm moved elsewhere
            Level[i][j] = null;
        } else if (!thisWorm.looseWeight()) {
            //dead worm
            Level[i][j] = null;
        } else {
            evolveIfStuck(thisWorm);
        }

        checkOverweightAndMultiplyIfNeeded(i, j, thisWorm, newX, newY);
        thisWorm.setChecked(true);
    }

    private void moveWormAndEatBacteriaIfAble(Worm thisWorm, int newX, int newY) {
        if (Level[newX][newY] != null && Level[newX][newY].is(Bacteria.class)) {
            Bacteria thisBact = (Bacteria) Level[newX][newY];
            thisWorm.eatBacteria(thisBact.getEaten());
        }
        Level[newX][newY] = thisWorm;
        if (!thisWorm.looseWeight()) {
            Level[newX][newY] = null;
        }
    }

    private void evolveIfStuck(Worm thisWorm) {
        thisWorm.stuckCount++;
        if (thisWorm.stuckCount >= Constants.MAX_STUCK && thisWorm.isUnderweight()) {
            thisWorm.forceMutation();
            thisWorm.stuckCount = 0;
        }
    }

    private void checkOverweightAndMultiplyIfNeeded(int i, int j, Worm thisWorm, int newX, int newY) {
        HexDirection direction;
        if (thisWorm.isOverweight()) {
            for (int k = 0; k < 6; k++) {
                direction = WormUtils.getRandomDirection();
                newX = TranslateDirection(i, j, direction, 'x');
                newY = TranslateDirection(i, j, direction, 'y');
                if (newX >= 0 && newX < levelSize && newY >= 0 && newY < levelSize && Level[newX][newY] == null)
                    break;
            }
            if (newX >= 0 && newX < levelSize && newY >= 0 && newY < levelSize && Level[newX][newY] == null) {
                Worm newborn = new Worm(thisWorm);
                newborn.setChecked(true);
                Level[newX][newY] = newborn;
                newborn = new Worm(thisWorm);
                newborn.setChecked(true);
                Level[i][j] = newborn;
            }
        }
    }

    private void resetWormsChecked() {
        for (int i = 0; i < levelSize; i++) {
            for (int j = 0; j < levelSize; j++) {
                if (Level[i][j] != null && Level[i][j].is(Worm.class)) {
                    ((Worm) Level[i][j]).setChecked(false);
                }
            }
        }
    }

    private void spawnRandomBacteries() {
        for (int i = 0; i < Constants.BACTERIA_GROWTH; i++) {
            r.setSeed(System.currentTimeMillis());
            int x = r.nextInt(levelSize);
            int y = r.nextInt(levelSize);
            if (Level[x][y] == null) {
                Level[x][y] = new Bacteria();
            }
        }
    }

    private int TranslateDirection(int x, int y, HexDirection d, char c){
        if(c == 'y'){
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
        }else if(c == 'x'){
            if(y%2 == 0){
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
            }else{
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
            }
        }
        return 0;
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

    public int getFieldMass(int x, int y) {

        if (this.Level[x][y] == null) {
            return -1;
        }

        // Worm or Bacteria
        return this.Level[x][y].getMass();
    }


    public static void main(String[] args) throws InterruptedException {
        //Code under should be launched by button?
        Game thisGame = new Game(Constants.LEVEL_SIZE); // Calling constructor & adding some worms

        thisGame.startGame();
    }

    private void startGame() throws InterruptedException {
        OknoClass gameWindow = new OknoClass();
        while(true) {
            while (Constants.isRunning) {
                makeStep();      // Updating Level state
                gameWindow.redrawWindow(this);

                try {
                    Thread.sleep(Constants.turnLengthInMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(!Constants.isRunning && Constants.nextStep == 1) {
                makeStep();

                gameWindow.redrawWindow(this);
                Constants.nextStep = 0;
            }

            Thread.sleep(1);
        }
    }
}



