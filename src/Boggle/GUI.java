package Boggle;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class GUI extends JFrame implements ActionListener{

    JLabel[][] grid;
    JPanel upper, middle, lower;
    JPanel scoreBoard;
    JLabel p1, p2, title;

    public GUI() {
        setSize(500, 500);
        setName("BOGGLE!");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));



        middle = new JPanel(new GridLayout(5,5));

        grid = new JLabel[5][5];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new JLabel("Test");
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                middle.add(grid[i][j]);
            }
        }
        add(middle);

        lower = new JPanel();
        lower.setLayout(new BoxLayout(lower, BoxLayout.Y_AXIS));

        title = new JLabel("Score");
        lower.add(title);

        scoreBoard = new JPanel(new FlowLayout());
        p1 = new JLabel("0");
        scoreBoard.add(p1);
        p2 = new JLabel("0");
        scoreBoard.add(p2);
        lower.add(scoreBoard);

        add(lower);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
