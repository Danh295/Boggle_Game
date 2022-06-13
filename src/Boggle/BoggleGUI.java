package Boggle;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class BoggleGUI extends JFrame implements ActionListener{

    JLabel[][] grid;
    JPanel upper, middle, lower;    //  3 main panels
    JPanel buttons, scoreBoard;     //  Panels within panels
    JButton start, pause,
    JLabel p1, p2, title, instructions;


    public BoggleGUI() {

        setSize(600, 600);
        setName("BOGGLE!");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        upper = new JPanel(new FlowLayout());
        instructions = new JLabel("Welcome to Boggle! The goal of the game is to find more and longer words than the other player!");
        upper.add(instructions);

        middle = new JPanel(new FlowLayout());




        lower = new JPanel(new GridLayout(5,5));

        grid = new JLabel[5][5];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new JLabel("Test");
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                lower.add(grid[i][j]);
            }
        }
        add(lower);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
