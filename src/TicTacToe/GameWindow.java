import javax.swing.*;
import java.awt.*;

class GameWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 560;
    private static final int WINDOW_WIDTH = 510;
    private static final int WINDOW_POS_X = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - WINDOW_WIDTH / 2;
    private static final int WINDOW_POS_Y = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - WINDOW_HEIGHT / 2;

    private StartNewGameWindow startNewGameWindow;
    private Map map;

    GameWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(WINDOW_POS_X, WINDOW_POS_Y);
        setTitle("Крестики-Нолики");
        setResizable(false);
        JButton btnGameNew = new JButton("Новая игра");
        btnGameNew.addActionListener(e -> startNewGameWindow.setVisible(true));
        JButton btnGameExit = new JButton("Выход");
        btnGameExit.addActionListener(e -> System.exit(0));
        map = new Map();

        JPanel panelBottom = new JPanel();
        panelBottom.setLayout(new GridLayout(1, 2));
        panelBottom.add(btnGameNew);
        panelBottom.add(btnGameExit);
        add(map, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);

        startNewGameWindow = new StartNewGameWindow(this);
        setVisible(true);
    }

    void startNewGame(int mode, int sizeFieldX, int sizeFieldY, int winLength) {
        map.startNewGame(mode, sizeFieldX, sizeFieldY, winLength);
    }
}
