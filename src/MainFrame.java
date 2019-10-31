import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MainFrame extends JFrame {

    private JPanel MainPanel = new JPanel();

    MainFrame() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        this.setTitle("Tic-Tac-Toe and a borrowed car");
        this.setSize(850, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        MainPanel.setLayout(new BorderLayout());
        MainPanel.add(getMenuPanel());

        this.add(MainPanel);
        this.setVisible(true);
    }

    private JPanel getMenuPanel() {
        TicTacToe tic = new TicTacToe();
        tic.getResetButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restart();
            }
        });
        return tic.getGamePanel();
    }

    private void restart() {

        MainPanel.removeAll();
        MainPanel.add(getMenuPanel());
        revalidate();
        repaint();
    }
}


