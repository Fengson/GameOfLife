import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    static boolean isRunning = false;
    static int nextStep = 0;
    static int gameSpeed;

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

        // Panel Opcji - Główny Panel
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setPreferredSize(new Dimension(800, 50));

        // Panel Przycisków
        JPanel buttonsPanel = new JPanel();
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton frameButton = new JButton("1 Frame");

        startButton.setPreferredSize(new Dimension(100, 40));
        stopButton.setPreferredSize(new Dimension(100, 40));
        frameButton.setPreferredSize(new Dimension(100, 40));

        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        buttonsPanel.add(frameButton);

        // Panel Slidera
        JPanel sliderPanel = new JPanel();
        final JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 11, 9);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintLabels(true);
        speedSlider.setPreferredSize(new Dimension(350, 50));

        final JLabel sliderValue = new JLabel();
        sliderValue.setText((int)Math.pow(2, speedSlider.getValue()) + "ms");

        // Wartość początkowa
        if(gameSpeed == 0){
            gameSpeed = (int)Math.pow(2, speedSlider.getValue());
        }

        // Slider Listener
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentValue = (int)Math.pow(2,speedSlider.getValue());
                gameSpeed = currentValue;
                sliderValue.setText(currentValue + "ms");
            }
        });

        sliderPanel.add(speedSlider);
        sliderPanel.add(sliderValue);

        optionsPanel.add(buttonsPanel, BorderLayout.WEST);
        optionsPanel.add(sliderPanel, BorderLayout.CENTER);

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
            public Dimension getPreferredSize() { return new Dimension(windowSize + 2*radius, windowSize); }
        };

        mainWindow.add(optionsPanel, BorderLayout.NORTH);
        mainWindow.add(gamePanel, BorderLayout.SOUTH);
        mainWindow.pack();
        mainWindow.setVisible(true);

        // Action Listenery do Przycisków
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isRunning = true;
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isRunning = false;
            }
        });

        frameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextStep = 1;
            }
        });
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

    public static void main(String[] args) throws InterruptedException {

        int xTable;
        int fieldKind;

        OknoClass gameWindow = new OknoClass();
        xTable = gameWindow.getX();

        //Code under should be launched by button?
        Game thisGame = new Game(xTable); // Calling constructor & adding some worms

        while(true) {
            while (isRunning) {
                thisGame.MakeStep();      // Updating Level state

                for (int i = 0; i < xTable; i++) {
                    for (int j = 0; j < xTable; j++) {
                        fieldKind = thisGame.getFieldKind(i, j);
                        gameWindow.gameArray[i][j] = fieldKind;
                    }
                }

                gameWindow.gamePanel.repaint();

                try {
                    Thread.sleep(gameSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            if(!isRunning && nextStep == 1) {
                thisGame.MakeStep();

                for (int i = 0; i < xTable; i++) {
                    for (int j = 0; j < xTable; j++) {
                        fieldKind = thisGame.getFieldKind(i, j);
                        gameWindow.gameArray[i][j] = fieldKind;
                    }
                }

                gameWindow.gamePanel.repaint();
                nextStep = 0;
            }

            Thread.sleep(1);
        }

    }

}
