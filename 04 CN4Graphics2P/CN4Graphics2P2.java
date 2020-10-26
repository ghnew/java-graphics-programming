import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Tic-Tac-Toe: Two-player Graphics version with Simple-OO
 */
@SuppressWarnings("serial")
public class CN4Graphics2P extends JFrame {
    // Named-constants for the game board
    public static final int ROWS = 6; // ROWS by COLS cells
    public static final int COLS = 7;

    // Named-constants of the various dimensions used for graphics drawing
    public static final int CELL_SIZE = 100; // cell width and height (square)
    public static final int CANVAS_WIDTH = CELL_SIZE * COLS; // the drawing canvas
    public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
    public static final int GRID_WIDTH = 3; // Grid-line's width
    public static final int GRID_WIDHT_HALF = GRID_WIDTH / 2; // Grid-line's half-width
    // Symbols (cross/nought) are displayed inside a cell, with padding from border
    public static final int CELL_PADDING = CELL_SIZE / 6;
    public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2; // width/height
    public static final int SYMBOL_STROKE_WIDTH = 3; // pen's stroke width

    // Use an enumeration (inner class) to represent the various states of the game
    public enum GameState {
        PLAYING,
        DRAW,
        CROSS_WON,
        NOUGHT_WON
    }
    private GameState currentState; // the current game state

    // Use an enumeration (inner class) to represent the seeds and cell contents
    public enum Seed {
        EMPTY,
        CROSS,
        NOUGHT
    }
    private Seed currentPlayer; // the current player

    private Seed[][] board; // Game board of ROWS-by-COLS cells
    private DrawCanvas canvas; // Drawing canvas (JPanel) for the game board
    private JLabel statusBar; // Status Bar

    /** Constructor to setup the game and the GUI components */
    public CN4Graphics2P() {
        canvas = new DrawCanvas(); // Construct a drawing canvas (a JPanel)
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        // The canvas (JPanel) fires a MouseEvent upon mouse-click
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { // mouse-clicked handler
                int mouseX = e.getX();
                int mouseY = e.getY();
                // Get the row and column clicked
                int rowSelected = mouseY / CELL_SIZE;
                int colSelected = mouseX / CELL_SIZE;

                if (currentState == GameState.PLAYING) {
                    if (colSelected >= 0 && colSelected < COLS) {
                        // Look for an empty cell starting from the bottom row
                        for (int row = ROWS - 1; row >= 0; row--) {
                            if (board[row][colSelected] == Seed.EMPTY) {
                                board[row][colSelected] = currentPlayer; // Make a move
                                updateGame(currentPlayer, row, colSelected); // update state
                                // Switch player
                                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
                                break;
                            }
                        }
                    }
                } else { // game over
                    initGame(); // restart the game
                }
                // Refresh the drawing canvas
                repaint(); // Call-back paintComponent().
            }
        });

        // Setup the status bar (JLabel) to display status message
        statusBar = new JLabel("  ");
        statusBar.setFont(new Font("Roboto Condensed", Font.BOLD, 13));
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(canvas, BorderLayout.CENTER);
        cp.add(statusBar, BorderLayout.PAGE_END); // same as SOUTH

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack(); // pack all the components in this JFrame
        setTitle("CN4Graphics2P");
        setVisible(true); // show this JFrame

        board = new Seed[ROWS][COLS]; // allocate array
        initGame(); // initialize the game board contents and game variables
    }

    /** Initialize the game-board contents and the status */
    public void initGame() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = Seed.EMPTY; // all cells empty
            }
        }
        currentState = GameState.PLAYING; // ready to play
        currentPlayer = Seed.CROSS; // cross plays first
    }

    /** Update the currentState after the player with "theSeed" has placed on
        (rowSelected, colSelected). */
    public void updateGame(Seed theSeed, int rowSelected, int colSelected) {
        if (hasWon(theSeed)) { // check for win
            currentState = (theSeed == Seed.CROSS) ? GameState.CROSS_WON : GameState.NOUGHT_WON;
        } else if (isDraw()) { // check for draw
            currentState = GameState.DRAW;
        }
        // Otherwise, no change to current state (still GameState.PLAYING).
    }

    /** Return true if it is a draw (i.e., no more empty cell) */
    public boolean isDraw() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] == Seed.EMPTY) {
                    return false; // an empty cell found, not draw, exit
                }
            }
        }
        return true; // no more empty cell, it's a draw
    }

    /** Return true if the player with "theSeed" has won after placing at
        (rowSelected, colSelected) */
    public boolean hasWon(Seed theSeed) {
        //check for 4 across
        for(int row = 0; row < ROWS; row++){
            for (int col = 0; col < COLS - 3;col++){
                if (board[row][col] == theSeed   && 
                    board[row][col+1] == theSeed &&
                    board[row][col+2] == theSeed &&
                    board[row][col+3] == theSeed){
                    return true;
                }
            }           
        }
        //check for 4 up and down
        for(int row = 0; row < ROWS - 3; row++){
            for(int col = 0; col < COLS; col++){
                if (board[row][col] == theSeed   && 
                    board[row+1][col] == theSeed &&
                    board[row+2][col] == theSeed &&
                    board[row+3][col] == theSeed){
                    return true;
                }
            }
        }
        //check upward diagonal
        for(int row = 3; row < ROWS; row++){
            for(int col = 0; col < COLS - 3; col++){
                if (board[row][col] == theSeed   && 
                    board[row-1][col+1] == theSeed &&
                    board[row-2][col+2] == theSeed &&
                    board[row-3][col+3] == theSeed){
                    return true;
                }
            }
        }
        //check downward diagonal
        for(int row = 0; row < ROWS - 3; row++){
            for(int col = 0; col < COLS - 3; col++){
                if (board[row][col] == theSeed   && 
                    board[row+1][col+1] == theSeed &&
                    board[row+2][col+2] == theSeed &&
                    board[row+3][col+3] == theSeed){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     *  Inner class DrawCanvas (extends JPanel) used for custom graphics drawing.
     */
    class DrawCanvas extends JPanel {
        @Override
        public void paintComponent(Graphics g) { // invoke via repaint()
            super.paintComponent(g); // fill background
            setBackground(Color.WHITE); // set its background color

            // Draw the grid-lines
            g.setColor(Color.LIGHT_GRAY);
            for (int row = 1; row < ROWS; ++row) {
                g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDHT_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
            }
            for (int col = 1; col < COLS; ++col) {
                g.fillRoundRect(CELL_SIZE * col - GRID_WIDHT_HALF, 0,
                    GRID_WIDTH, CANVAS_HEIGHT - 1, GRID_WIDTH, GRID_WIDTH);
            }

            // Draw the Seeds of all the cells if they are not empty
            // Use Graphics2D which allows us to set the pen's stroke
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND)); // Graphics2D only
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    int x1 = col * CELL_SIZE + CELL_PADDING;
                    int y1 = row * CELL_SIZE + CELL_PADDING;
                    if (board[row][col] == Seed.CROSS) {
                        g2d.setColor(Color.RED);
                        int x2 = (col + 1) * CELL_SIZE - CELL_PADDING;
                        int y2 = (row + 1) * CELL_SIZE - CELL_PADDING;
                        g2d.drawLine(x1, y1, x2, y2);
                        g2d.drawLine(x2, y1, x1, y2);
                    } else if (board[row][col] == Seed.NOUGHT) {
                        g2d.setColor(Color.BLUE);
                        g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                    }
                }
            }

            // Print status-bar message
            if (currentState == GameState.PLAYING) {
                statusBar.setForeground(Color.BLACK);
                if (currentPlayer == Seed.CROSS) {
                    statusBar.setText("X's Turn".toUpperCase());
                } else {
                    statusBar.setText("O's Turn".toUpperCase());
                }
            } else if (currentState == GameState.DRAW) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("It's a Draw! Click to play again.".toUpperCase());
            } else if (currentState == GameState.CROSS_WON) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("'X' Won! Click to play again.".toUpperCase());
            } else if (currentState == GameState.NOUGHT_WON) {
                statusBar.setForeground(Color.RED);
                statusBar.setText("'O' Won! Click to play again.".toUpperCase());
            }
        }
    }

    /** The entry main() method */
    public static void main(String[] args) {
        // Run GUI codes in the Event-Dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CN4Graphics2P(); // Let the constructor do the job
            }
        });
    }
}