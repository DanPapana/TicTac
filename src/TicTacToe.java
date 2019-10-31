import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class TicTacToe {

    private JPanel GamePanel = new JPanel(new GridLayout(3, 3));
    private ImageIcon OIcon = new ImageIcon(new ImageIcon(MainFrame.class.getResource("/Images/O.png"))
            .getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH));
    private ImageIcon XIcon = new ImageIcon(new ImageIcon(MainFrame.class.getResource("/Images/X.png"))
            .getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH));

    private JButton[] TicTacButton = new JButton[9];
    private int player = 1;                         // 1 === Player 1 (X),  -1 === Player 2 (O)
    private int[] position = new int[9];            // 0 === empty, 1 === X, -1 === O
    private JTextPane message_area = new JTextPane();
    private JPanel MenuPanel = new JPanel();
    private JPanel SuperMainPanel = new JPanel(new BorderLayout());
    private JButton reset_button = new JButton();
    private JTextField difficulty_field = new JTextField();
    private int best_move = -1;

    TicTacToe() {

        GamePanel.setPreferredSize(new Dimension(600, 600));

        MenuPanel.setLayout(new BoxLayout(MenuPanel, BoxLayout.Y_AXIS));
        MenuPanel.setPreferredSize(new Dimension(250, 300));
        MenuPanel.add(Box.createRigidArea(new Dimension(0, 100)));

        JPanel messagePanel = new JPanel();
        message_area.setPreferredSize(new Dimension(170, 40));
        message_area.setBackground(new Color(0xFFBCDEF5, true));
        message_area.setEditable(false);
        messagePanel.add(message_area);

        JPanel difficultyPanel = new JPanel();
        difficulty_field.setText("2");
        difficulty_field.setPreferredSize(new Dimension(70, 20));

        difficultyPanel.add(Box.createRigidArea(new Dimension(0, 150)));
        difficultyPanel.add(new JLabel("Difficulty:"));
        difficultyPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        difficultyPanel.add(difficulty_field);

        JPanel resetPanel = new JPanel();
        reset_button.setText("NEW GAME");
        reset_button.setPreferredSize(new Dimension(150, 50));
        resetPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        resetPanel.add(reset_button);

        MenuPanel.add(messagePanel);
        MenuPanel.add(difficultyPanel);
        MenuPanel.add(resetPanel);

        for (int i = 0; i < 9; i++) {
            TicTacButton[i] = new JButton();
            TicTacButton[i].setBackground(Color.yellow);
            TicTacButton[i].setContentAreaFilled(false);
            TicTacButton[i].setOpaque(true);
            TicTacButton[i].addActionListener(new buttonListener());
            position[i] = 0;

            if (i % 2 == 0) {
                TicTacButton[i].setBackground(Color.orange);
            }
            GamePanel.add(TicTacButton[i]);
        }

        SuperMainPanel.add(GamePanel, BorderLayout.WEST);
        SuperMainPanel.add(MenuPanel, BorderLayout.EAST);
    }

    JButton getResetButton() {
        return reset_button;
    }

    JPanel getGamePanel() {
        return SuperMainPanel;
    }

    private int checkWin(int[] position) {

        //returns 0 for ongoing game, returns 1 for player 1 (player, X), returns -1 for player 2 (AI, O)
        //the default position in all cells is set to 0
        //when there's an X in a cell, the position is equal to 1, if it's O then -1

        if (checkDiagonals(position) == 1 || checkVerticals(position) == 1 || checkHorizontals(position) == 1) {
            return 1;
        }
        if (checkDiagonals(position) == -1 || checkVerticals(position) == -1 || checkHorizontals(position) == -1) {
            return -1;
        }

        return 0;
    }

    private int checkDiagonals(int[] position) {

        //checks diagonally
        if (position[0] + position[4] + position[8] == 3 || position[2] + position[4] + position[6] == 3) {
            return 1;
        } else if (position[0] + position[4] + position[8] == -3 || position[2] + position[4] + position[6] == -3) {
            return -1;
        }
        return 0;
    }

    private int checkVerticals(int[] position) {

        //checks vertically
        for (int i = 0; i < 3; i++) {
            if (position[i * 3] + position[i * 3 + 1] + position[i * 3 + 2] == 3) {
                return 1;
            }
            if (position[i * 3] + position[i * 3 + 1] + position[i * 3 + 2] == -3) {
                return -1;
            }
        }
        return 0;
    }

    private int checkHorizontals(int[] position) {

        //checks horizontally
        for (int i = 0; i < 3; i++) {
            if (position[i] + position[i + 3] + position[i + 6] == 3) {
                return 1;
            }
            if (position[i] + position[i + 3] + position[i + 6] == -3) {
                return -1;
            }
        }
        return 0;
    }

    private int availablePaths(int[] position, int k) {

        //Checks available paths
        int totalOpen = 0;

        if (position[0] != k && position[4] != k && position[8] != k) {
            totalOpen++;
        }

        if (position[2] != k && position[4] != k && position[6] != k) {
            totalOpen++;
        }

        for (int i = 0; i < 3; i++) {
            if (position[i] != k && position[i + 3] != k && position[i + 6] != k) {
                totalOpen++;
            }
            if (position[i * 3] != k && position[i * 3 + 1] != k && position[i * 3 + 2] != k) {
                totalOpen++;
            }
        }
        return totalOpen;
    }

    private int miniMax(int[] this_position, int depth, int current_player, int alpha, int beta) {

        if (checkWin(this_position) != 0) {
            return checkWin(this_position) * current_player;
        }

        if (depth == 0 || availablePaths(this_position, current_player) == 0) {
            return (availablePaths(this_position, -1) - availablePaths(this_position,1));
        }

        int bestValue = -1000;

        for (int i = 0; i < 9; i++) {

            if (this_position[i] == 0) {

                this_position[i] = current_player;

                int value = -miniMax(this_position, depth - 1, -current_player, -alpha, -beta);

                this_position[i] = 0;

                if (value > bestValue) {
                    bestValue = value;
                    best_move = i;

                    if (bestValue > alpha) {
                        alpha = bestValue;
                        if (alpha >= beta) {
                            return bestValue;
                        }
                    }

                }
            }
        }

        if (best_move == -1) {
            return 0;
        }

        return bestValue;
    }

    class buttonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {

            if (player == 1) {

                for (int i = 0; i < 9; i++) {

                    if (event.getSource() == TicTacButton[i] && position[i] == 0) {

                        message_area.setBackground(new Color(0xC2F9C4));
                        TicTacButton[i].setIcon(XIcon);
                        position[i] = 1;
                        player = -1;

                        if (checkWin(position) == 1) {
                            message_area.setText("\n       🥇 ❌  HUMAN WON ❌ 🥇\n");
                            player = 0;
                        }
                    }
                }

                if (player == -1) {

                    miniMax(position, Integer.parseInt(difficulty_field.getText()), player, -1000, 1000);

                    TicTacButton[best_move].setIcon(OIcon);
                    position[best_move] = -1;
                    player = 1;

                    if (checkWin(position) == -1) {
                        message_area.setText("\n       🥇 🏐 COMPUTER WON 🏐 🥇\n");
                        player = 0;
                    }
                }
            }
        }
    }
}