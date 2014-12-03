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
    private final int xTable  = Constants.LEVEL_SIZE;
    private final int yTable  = Constants.LEVEL_SIZE;
    private final int windowSize = 800;
    private final int radius = windowSize/(2*xTable);
    private int gameArray[][];
    private int massArray[][];

    public OknoClass() {
        initComponents();
        gameArray = new int[xTable][yTable];
        massArray = new int[xTable][yTable];
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
        final JCheckBox massCheckBox = new JCheckBox("Mass");
        massCheckBox.setSelected(false);

        startButton.setPreferredSize(new Dimension(100, 40));
        stopButton.setPreferredSize(new Dimension(100, 40));
        frameButton.setPreferredSize(new Dimension(100, 40));

        buttonsPanel.add(startButton);
        buttonsPanel.add(stopButton);
        buttonsPanel.add(frameButton);
        buttonsPanel.add(massCheckBox);

        // Panel Slidera
        JPanel sliderPanel = new JPanel();
        final JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 1, 11, 9);
        speedSlider.setMajorTickSpacing(1);
        speedSlider.setPaintLabels(true);
        speedSlider.setPreferredSize(new Dimension(350, 50));

        final JLabel sliderValue = new JLabel();
        sliderValue.setText((int)Math.pow(2, speedSlider.getValue()) + "ms");

        // Wartość początkowa
        if(Constants.turnLengthInMs == 0){
            Constants.turnLengthInMs = (int)Math.pow(2, speedSlider.getValue());
        }

        // Slider Listener
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Constants.turnLengthInMs = (int)Math.pow(2,speedSlider.getValue());
                sliderValue.setText(Constants.turnLengthInMs + "ms");
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

                int originalX = radius + radius / 2;
                int x = radius + radius / 2;
                int y = radius + radius / 2;

                for (int j = 0; j < yTable; j++) {
                    for(int i=0; i<xTable; i++){

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

                        int rememberX = x;
                        g2.setColor(Color.BLACK);

                        if(massArray[i][j] < 10){
                            x = x - 4;
                        }else {
                            x = x - 8;
                        }

                        if(massCheckBox.isSelected()) {
                            if(massArray[i][j] == -1)
                                g2.drawString("", x, y + 4);
                            else
                                g2.drawString(String.valueOf(massArray[i][j]), x, y + 4);
                        }

                        x = rememberX + 2 * radius;
                    }

                    if(j % 2 == 0) {
                        x = originalX + radius;
                    } else {
                        x = originalX;
                    }

                    y = y + 2 * radius - originalX/4;
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
                Constants.isRunning = true;
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Constants.isRunning = false;
            }
        });

        frameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Constants.nextStep = 1;
            }
        });
    }

    // Metoda do rysowania Hexagonów
    // Pobiera ilość kolumn i rzędów, rozmiar, oraz pozycję pierwszego hexagona
    public void createHexagons(int hexagonColumns, int hexagonRows, int r, int x, int y){

        int originalX = x;

        for(int j=0; j<hexagonRows; j++){
            for(int i=0; i<hexagonColumns; i++){

                singleHexagon = new Polygon();
                for(int k=0; k<6; k++) {
                    singleHexagon.addPoint((int)(x + r * Math.cos(k * 2 * Math.PI / 6 + Math.toRadians(30))), (int)(y + r * Math.sin(k * 2 * Math.PI/6 + Math.toRadians(30))));
                }

                hexagonArray[i][j] = singleHexagon;

                x = x + 2*r;
            }

            if(j % 2 == 0) {
                x = originalX + r;
            } else {
                x = originalX;
            }

            y = y + 2 * r - originalX/4;
        }
    }

    int fieldKind;
    int fieldMass;

    public void redrawWindow(Game thisGame) {
        for (int i = 0; i < xTable; i++) {
            for (int j = 0; j < xTable; j++) {
                fieldKind = thisGame.getFieldKind(i, j);
                fieldMass = thisGame.getFieldMass(i, j);

                gameArray[i][j] = fieldKind;
                massArray[i][j] = fieldMass;
            }
        }

        gamePanel.repaint();
    }

}