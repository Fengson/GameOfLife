import javax.swing.*;
import java.awt.*;

/**
 * Created by Fengson on 23.11.14.
 */
public class OknoClass {

    private JFrame mainWindow;
    private Polygon[][] hexagonArray;
    private Polygon singleHexagon;
    private int xTable, yTable, radius, windowSize;

    public OknoClass() {
        initComponents();
    }

    private void initComponents() {

        mainWindow = new JFrame("Game of Life");
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Ilość hexagonów jest uzależniona od wielkości okna
        windowSize = 800;
        // Wymagamy tablicy NxN, możemy tu ręcznie wstawić wartość
        xTable = 40;

        yTable = xTable;
        radius = windowSize/(2*xTable);

        hexagonArray = new Polygon[xTable][yTable];

        // Ostatnie dwie współrzędne to środek pierwszego Hexagona - (radius,radius) ustawia go w lewym górnym rogu
        createHexagons(xTable, yTable, radius, radius + radius / 2, radius + radius / 2);

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                for(int i=0; i<xTable; i++) {
                    for (int j = 0; j < yTable; j++) {

                        Graphics2D g2 = (Graphics2D) g;
                        g2.setStroke(new BasicStroke((float)4.75));

                        g2.setColor(Color.BLACK);
                        g2.drawPolygon(hexagonArray[i][j]);

                        g2.setColor(Color.WHITE);
                        g2.fillPolygon(hexagonArray[i][j]);
                    }
                }

            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(windowSize + 2*radius, windowSize);
            }
        };

        JButton button1 = new JButton("Start");
        button1.setBounds(10, windowSize - 35, 100, 25);

        mainWindow.add(button1);

        button1.setPreferredSize(new Dimension(300,300));


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
        new OknoClass();
    }
}
