import javax.swing.*;
import java.awt.*;

/**
 * Created by Fengson on 23.11.14.
 */
public class OknoClass {

    private JPanel gamePanel;
    private JFrame mainWindow;
    private Polygon[][] hexagonArray;
    private Polygon singleHexagon;
    private final int xTable  = 40;
    private final int yTable  = 40;
    private final int windowSize = 800;
    private final int radius = windowSize/(2*xTable);
    private int gameArray[][];

    public OknoClass() {
        initComponents();
        gameArray = new int[xTable][yTable];
    }

    public int getX(){ return xTable;}

    private void initComponents() {

        mainWindow = new JFrame("Game of Life");
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        hexagonArray = new Polygon[xTable][yTable];

        // Ostatnie dwie współrzędne to środek pierwszego Hexagona - (radius,radius) ustawia go w lewym górnym rogu
        createHexagons(xTable, yTable, radius, radius + radius / 2, radius + radius / 2);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for(int i=0; i<xTable; i++) {
                    for (int j = 0; j < yTable; j++) {

                        Graphics2D g2 = (Graphics2D) g;
                        g2.setStroke(new BasicStroke((float)4.75));

                        g2.setColor(Color.BLACK);
                        g2.drawPolygon(hexagonArray[i][j]);

                        // Empty Field
                        if(gameArray[i][j] == 0) {
                            g2.setColor(Color.WHITE);
                            g2.fillPolygon(hexagonArray[i][j]);
                        }

                        // Worm Field
                        if(gameArray[i][j] == 1) {
                            g2.setColor(Color.GREEN);
                            g2.fillPolygon(hexagonArray[i][j]);
                        }

                        // Bactery Field
                        if(gameArray[i][j] == 2) {
                            g2.setColor(Color.RED);
                            g2.fillPolygon(hexagonArray[i][j]);
                        }
                    }
                }

            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(windowSize + 2*radius, windowSize);
            }
        };

        JButton startButton = new JButton("Start");
        startButton.setBounds(10, windowSize - 35, 100, 25);

        JButton stopButton = new JButton("Stop");
        stopButton.setBounds(110, windowSize - 35, 100, 25);

        mainWindow.add(startButton);
        mainWindow.add(stopButton);

        startButton.setPreferredSize(new Dimension(300,300));
        stopButton.setPreferredSize(new Dimension(300,300));


        mainWindow.add(gamePanel);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    // Metoda do rysowania Hexagonów
    // Pobiera ilość kolumn i rzędów, rozmiar, oraz pozycję pierwszego hexagona
    public void createHexagons(int hexagonColumns, int hexagonRows, int r, int x, int y){

        int originalX = x;

        for(int i=0; i<hexagonColumns; i++){
            for(int j=0; j<hexagonRows; j++){

                singleHexagon = new Polygon();
                for(int k=0; k<6; k++) {
                    singleHexagon.addPoint((int)(x + r * Math.cos(k * 2 * Math.PI / 6 + Math.toRadians(30))), (int)(y + r * Math.sin(k * 2 * Math.PI/6 + Math.toRadians(30))));
                }

                hexagonArray[i][j] = singleHexagon;

                x = x + 2*r;
            }

            if(i % 2 == 0) {
                x = originalX + r;
            } else {
                x = originalX;
            }

            y = y + 2 * r - originalX/4;
        }
    }

    public static void main(String[] args) {

        int xTable;
        int fieldKind;

        OknoClass gameWindow = new OknoClass();
        xTable = gameWindow.getX();

        //Code under should be launched by button?
        Game thisGame = new Game(xTable); // Calling constructor & adding some worms
        boolean isRunning = true;
        while (isRunning){
            thisGame.MakeStep();      // Updating Level state

            for(int i=0; i<xTable; i++){
                for(int j=0; j<xTable; j++){
                    fieldKind = thisGame.getFieldKind(i,j);
                    gameWindow.gameArray[i][j] = fieldKind;
                }
            }

            gameWindow.gamePanel.repaint();

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //isRunning = false;
        }
    }

}
