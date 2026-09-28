package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import entities.miniPeople.MiniPerson;
import entities.dataObjects.Gender;
import populationBoard.BoardController;
import populationBoard.dataObjects.Board;
import populationBoard.dataObjects.CoordinatePair;
import static utilities.PrintHelper.print;
import static utilities.PrintHelper.println;


public class Game {
    private static Board board;
    private static BoardController controller;
    boolean runLoop;
    int sizeX = 10;
    int sizeY = 10;
    long delay;
    private JFrame frame;
    private JPanel panel;

    public Game(){
    }

    public void size(int x, int y){
        sizeX = x;
        sizeY = y;
        board = new Board(sizeX, sizeY);
        controller = new BoardController(board);
    }

    public void population(int population){
        controller.initializePopulation(population);
    }
    public void simSpeed(long time){
        delay = time;
    }

    public void start() {
        runLoop = true;

        while(runLoop){
            try {
                controller.moveAllRandomly();
                renderInGUI();
                Thread.sleep(delay);
                if (board.getPopulationMap().isEmpty()) {
                    runLoop = false;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // the AWT event thread is non-daemon, so the JVM won't exit while the frame is displayable
        if (frame != null) {
            frame.dispose();
        }
    }


    public void renderInConsole() {
        char[][] arrayToShow = new char[sizeX][sizeY];

        // Fill with empty space (otherwise you get '\0' chars)
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                arrayToShow[i][j] = ' ';
            }
        }

        HashMap<CoordinatePair, MiniPerson> hs = board.getPopulationMap();

        for (CoordinatePair cp : hs.keySet()) {
            MiniPerson p = hs.get(cp);
            if (p.getGender() == Gender.MALE) {
                arrayToShow[cp.getX()][cp.getY()] = 'M';
            } else if (p.getGender() == Gender.FEMALE) {
                arrayToShow[cp.getX()][cp.getY()] = 'F';
            }
        }

        // Top border: ┌───┬───┬───┐
        print("┌");
        for (int j = 0; j < sizeY; j++) {
            print("───");
            if (j < sizeY - 1) print("┬");
        }
        println("┐");

        // Rows
        for (int i = 0; i < sizeX; i++) {
            // Cell row: │ A │ B │ C │
            print("│");
            for (int j = 0; j < sizeY; j++) {
                print(" " + arrayToShow[i][j] + " │");
            }
            print();

            // Row separator (or bottom border on last row)
            if (i < sizeX - 1) {
                print("├");
                for (int j = 0; j < sizeY; j++) {
                    print("───");
                    if (j < sizeY - 1) print("┼");
                }
                println("┤");
            } else {
                print("└");
                for (int j = 0; j < sizeY; j++) {
                    print("───");
                    if (j < sizeY - 1) print("┴");
                }
                println("┘");
            }
        }
    }

    public void renderInGUI() {
        final int windowSize = 800;

        if(runLoop){
            if (frame == null || !frame.isDisplayable()) {
                frame = new JFrame("Board");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                panel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                        int cellSize = Math.min(windowSize / sizeX, windowSize / sizeY);
                        int offsetX = (getWidth()  - sizeX * cellSize) / 2;
                        int offsetY = (getHeight() - sizeY * cellSize) / 2;

                        // Grid lines
                        g2.setColor(Color.LIGHT_GRAY);
                        for (int i = 0; i <= sizeX; i++) {
                            int x = offsetX + i * cellSize;
                            g2.drawLine(x, offsetY, x, offsetY + sizeY * cellSize);
                        }
                        for (int j = 0; j <= sizeY; j++) {
                            int y = offsetY + j * cellSize;
                            g2.drawLine(offsetX, y, offsetX + sizeX * cellSize, y);
                        }

                        // Snapshot of the current population
                        HashMap<CoordinatePair, MiniPerson> hs = board.getPopulationMap();
                        g2.setFont(new Font("SansSerif", Font.BOLD, (int)(cellSize * 0.7)));
                        FontMetrics fm = g2.getFontMetrics();

                        for (CoordinatePair cp : hs.keySet()) {
                            MiniPerson mp = hs.get(cp);
                            String symbol = controller.names.getNameOf(mp);
                            Color color = mp.getGender() == Gender.MALE ? Color.BLUE : Color.RED;

                            if (symbol != null) {
                                g2.setColor(color);
                                int cellX = offsetX + cp.getX() * cellSize;
                                int cellY = offsetY + cp.getY() * cellSize;
                                int textWidth = fm.stringWidth(symbol);
                                int textHeight = fm.getAscent();
                                g2.drawString(symbol,
                                        cellX + (cellSize - textWidth) / 2,
                                        cellY + (cellSize + textHeight) / 2 - 2);
                            }
                        }

                        // Outer border
                        g2.setColor(Color.DARK_GRAY);
                        g2.setStroke(new BasicStroke(2));
                        g2.drawRect(offsetX, offsetY, sizeX * cellSize, sizeY * cellSize);
                    }
                };
                panel.setPreferredSize(new Dimension(windowSize, windowSize));
                panel.setBackground(Color.WHITE);

                frame.add(panel);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } else {
                panel.repaint();
            }
        }

    }

}