import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

class Map extends JPanel{

    private static final int GAME_MODE_H_VS_AI =    0;
    private static final int GAME_MODE_H_VS_H =     1;

    private static final int DOT_EMPTY =    0;
    private static final int DOT_X =        1;
    private static final int DOT_O =        2;

    private static final int STATE_DRAW =   0;
    private static final int STATE_X_WIN =  1;
    private static final int STATE_O_WIN =  2;
    private int stateGameOver;
    private int stateGameMode;

    private static final String MSG_DRAW =  "Ничья!";
    private static final String MSG_X_WIN = "Победил игрок_X!";
    private static final String MSG_O_WIN = "Победил игрок_O!";
    private boolean moveX = true;
    private boolean moveO = false;


    private final Random random = new Random();
    private final Font font = new Font("Times New Roman", Font.BOLD, 48);

    private int[][] gameField;
    private int winLength;
    private int fieldSizeX, fieldSizeY;
    private int cellWidth, cellHeight;
    private int cellX, cellY;
    private boolean initialized = false;
    private boolean gameOver;


    Map(){
        setBackground(Color.WHITE);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    void startNewGame(int mode, int fieldSizeX, int fieldSizeY, int winLength){
        this.stateGameMode = mode;
        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        this.winLength = winLength;
        gameField = new int[fieldSizeX][fieldSizeY];
        initialized = true;
        gameOver = false;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                update(e);
                repaint();
            }
        });
        repaint();
    }

    static int getGameModeHvsAi() {
        return GAME_MODE_H_VS_AI;
    }

    static int getGameModeHvsH() {
        return GAME_MODE_H_VS_H;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        render(g);
    }

    private void update(MouseEvent e) {
        cellX = e.getX() / cellWidth;
        cellY = e.getY() / cellHeight;
        if (gameOver) return;
        switch (stateGameMode) {
            case GAME_MODE_H_VS_AI: {
                modeHvsAi();
                break;
            }
            case GAME_MODE_H_VS_H: {
                modeHvsH();
            }
        }
    }

    private void modeHvsAi () {
        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellY, cellX)) {
            return;
        } else {
            gameField[cellX][cellY] = DOT_X;
        }
        if (checkWin(DOT_X)) {
            stateGameOver = STATE_X_WIN;
            gameOver = true;
            return;
        }
        if (isMapFull()) {
            stateGameOver = STATE_DRAW;
            gameOver = true;
            return;
        }
        repaint();
        aiMove();
        if (checkWin(DOT_O)) {
            stateGameOver = STATE_O_WIN;
            gameOver = true;
            return;
        }
        if (isMapFull()) {
            stateGameOver = STATE_DRAW;
            gameOver = true;
            return;
        }
        repaint();
    }

    private void modeHvsH() {
        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellY, cellX)) {
            return;
        } else if (moveX) {
            gameField[cellX][cellY] = DOT_X;
            moveX = !moveX;
            moveO = !moveO;
        }
        if(checkWin(DOT_X)) {
            stateGameOver = STATE_X_WIN;
            gameOver = true;
            return;
        }
        if(isMapFull()) {
            stateGameOver = STATE_DRAW;
            gameOver = true;
            return;
        }
        repaint();
        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellY, cellX)) {
            return;
        } else if (moveO) {
            gameField[cellX][cellY] = DOT_O;
            moveO = !moveO;
            moveX = !moveX;
        }
        if(checkWin(DOT_O)) {
            stateGameOver = STATE_O_WIN;
            gameOver = true;
            return;
        }
        if(isMapFull()) {
            stateGameOver = STATE_DRAW;
            gameOver = true;
            return;
        }
        repaint();
    }

    private void render(Graphics g){
        if(!initialized) return;
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        cellWidth = panelWidth / fieldSizeX;
        cellHeight = panelHeight / fieldSizeY;

        // рисуем горизонтальные и вертикальные линии поля
        g.setColor(Color.BLACK);
        for (int i = 0; i < fieldSizeY ; i++) {
            int y = i * cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }
        for (int i = 0; i < fieldSizeX; i++) {
            int x = i * cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }

        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (gameField[i][j] != DOT_EMPTY) {
                    // рисуем крестики
                    if (gameField[i][j] == DOT_X) {
                        g.setColor(Color.RED);
                        g.drawLine((i * cellWidth) + 10, (j * cellHeight) + 10, (i + 1) * cellWidth - 10, (j + 1) * cellHeight - 10);
                        g.drawLine((i + 1) * cellWidth - 10, (j * cellHeight) + 10, (i * cellWidth) + 10, (j + 1) * cellHeight - 10);
                    }
                    // рисуем нолики
                    else if (gameField[i][j] == DOT_O) {
                        g.setColor(Color.BLUE);
                        g.drawOval((i * cellWidth) + 10, (j * cellHeight) + 10, cellWidth - 20, cellHeight - 20);
                    } else {
                        throw new RuntimeException("Невозможно распознать значение ячейки: " + gameField[i][j]);
                    }
                }
            }
        }
        if(gameOver){
            showMessageGameOver(g);
        }
    }

    private void showMessageGameOver(Graphics g){
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0,200, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(font);
        switch (stateGameOver){
            case STATE_DRAW:
                g.drawString(MSG_DRAW, 180, getHeight() / 2);
                break;
            case STATE_X_WIN:
                g.drawString(MSG_X_WIN, 45, getHeight() / 2);
                break;
            case STATE_O_WIN:
                g.drawString(MSG_O_WIN, 45, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("Неожиданное состояние конца игры: " + stateGameOver);
        }
    }

    private void aiMove() {
        if(moveAIWinCell()) return;
        if(moveHumanWinCell()) return;
        int x, y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        gameField[y][x] = DOT_O;
    }

    private boolean moveAIWinCell() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    gameField[i][j] = DOT_O;
                    if (checkWin(DOT_O)) return true;
                    gameField[i][j] = DOT_EMPTY;
                }
            }
        }
        return false;
    }

    private boolean moveHumanWinCell() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    gameField[i][j] = DOT_X;
                    if (checkWin(DOT_X)) {
                        gameField[i][j] = DOT_O;
                        return true;
                    }
                    gameField[i][j] = DOT_EMPTY;
                }
            }
        }
        return false;
    }

    private boolean checkWin(int dot) {
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (checkLine(i, j, 1, 0, winLength, dot)) return true;
                if (checkLine(i, j, 1, 1, winLength, dot)) return true;
                if (checkLine(i, j, 0, 1, winLength, dot)) return true;
                if (checkLine(i, j, 1, -1, winLength, dot)) return true;
            }
        }
        return false;
    }

    private boolean checkLine(int x, int y, int vx, int vy, int len, int dot) {
        final int far_x = x + (len - 1) * vx;
        final int far_y = y + (len - 1) * vy;
        if (!isValidCell(far_x, far_y)) return false;
        for (int i = 0; i < len; i++) {
            if (gameField[y + i * vy][x + i * vx] != dot) return false;
        }
        return true;
    }

    private boolean isMapFull() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (gameField[i][j] == DOT_EMPTY) return false;
            }
        }
        return true;
    }

    private boolean isValidCell(int x, int y) { return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY; }

    private boolean isEmptyCell(int x, int y) { return gameField[y][x] == DOT_EMPTY; }

}

