import javax.swing.*;

public class TicTacToeClass {
    public static void main(String[] args) {
        // For Native Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception ignored){}

        /* Run the Frame */
        /* Базовый механизм запуска произвольного кода в потоке GUI — invokeLater */
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new GameWindow();
            }
        });
    }
}
