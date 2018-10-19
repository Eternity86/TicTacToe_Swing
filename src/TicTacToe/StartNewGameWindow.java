import javax.swing.*;
import java.awt.*;

class StartNewGameWindow extends JDialog{
    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 276;
    private static final int MIN_WIN_LENGTH = 3;
    private static final int MIN_FIELD_SIZE = 3;
    private static final int MAX_FIELD_SIZE = 10;

    private GameWindow gameWindow;
    private JRadioButton humVSAI;
    private JRadioButton humVShum;
    private JSlider slideFieldSizeX;
    private JSlider slideFieldSizeY;
    private JSlider slideWinLength;


    StartNewGameWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        setModal(true);
        setResizable(false);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        Rectangle gameWindowBounds = gameWindow.getBounds();
        int posX = (int)gameWindowBounds.getCenterX() - WINDOW_WIDTH / 2;
        int posY = (int)gameWindowBounds.getCenterY() - WINDOW_HEIGHT / 2;
        setLocation(posX, posY);
        setTitle("Настройки новой игры");
        setLayout(new GridLayout(12, 1));
        addGameControlsMode();
        addGameControlField();

        JButton btnStartGame = new JButton("Новая игра");
        btnStartGame.addActionListener(e -> btnStartGame_OnClick());
        add(btnStartGame);

    }
    private void addGameControlsMode() {
        add(new JLabel("Выбрать игровой режим"));
        humVSAI = new JRadioButton("Игрок против ИИ");
        humVShum = new JRadioButton("Игрок против Игрока");
        ButtonGroup gameMode = new ButtonGroup();
        gameMode.add(humVSAI);
        gameMode.add(humVShum);
        humVSAI.setSelected(true);
        add(humVSAI);
        add(humVShum);
    }

    private void addGameControlField() {
        final String FIELD_SIZE_X_PREFIX = "Размер поля по X: ";
        JLabel lbFieldSizeX = new JLabel(FIELD_SIZE_X_PREFIX + MIN_WIN_LENGTH);

        slideFieldSizeX = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
        slideFieldSizeX.addChangeListener(e -> {
            int currentValue = slideFieldSizeX.getValue();
            lbFieldSizeX.setText(FIELD_SIZE_X_PREFIX + currentValue);
            slideWinLength.setMaximum(currentValue);
        });

        /*final String FIELD_SIZE_PREFIX = "Размер поля по Y: ";
        JLabel lbFieldSizeY = new JLabel(FIELD_SIZE_PREFIX + slideFieldSizeX.getValue());

        slideFieldSizeY = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
        slideFieldSizeY.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentValue = slideFieldSizeY.getValue();
                lbFieldSizeY.setText(FIELD_SIZE_PREFIX + currentValue);
                //slideWinLength.setMaximum(currentValue);
            }
        });*/
        JLabel lbEmpty1 = new JLabel("");
        JLabel lbEmpty2 = new JLabel("");

        final String WIN_LEN_PREFIX = "Победная серия: ";
        JLabel lbWinLength = new JLabel(WIN_LEN_PREFIX + MIN_WIN_LENGTH);

        slideWinLength = new JSlider(MIN_WIN_LENGTH, slideFieldSizeX.getValue() /*< slideFieldSizeY.getValue() ? slideFieldSizeX.getX() : slideFieldSizeY.getValue()*/, MIN_WIN_LENGTH);
        slideWinLength.addChangeListener(e -> lbWinLength.setText(WIN_LEN_PREFIX + slideWinLength.getValue()));

        add(new JLabel("Укажите размер поля"));
        add(lbFieldSizeX);
        add(slideFieldSizeX);
        //add(lbFieldSizeY);
        //add(slideFieldSizeY);
        add(lbEmpty1); // пустые метки-заглушки вместо слайдера выбора размера поля по Y
        add(lbEmpty2); // при решении проблемы с разной размерностью поля - удалить эти две метки и раскомментированть метку и слайдер
        add(new JLabel("Укажите победную серию"));
        add(lbWinLength);
        add(slideWinLength);
    }

    private void btnStartGame_OnClick(){
        int gameMode;
        if(humVSAI.isSelected())
            gameMode = Map.getGameModeHvsAi();
        else if (humVShum.isSelected())
            gameMode = Map.getGameModeHvsH();
        else
            throw new RuntimeException("Ничего не выбрано");

        int sizeFieldX = slideFieldSizeX.getValue();
        //int sizeFieldY = slideFieldSizeY.getValue();
        int winLen = slideWinLength.getValue();
        gameWindow.startNewGame(gameMode, sizeFieldX, sizeFieldX, winLen);
        setVisible(false);
    }
}

