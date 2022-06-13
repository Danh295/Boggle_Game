package Boggle;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class BoggleGUI extends JFrame implements ActionListener{

    JLabel[][] grid;
    JPanel upper, middle, lower;    //  3 main panels
    JPanel buttons, scoreBoard;     //  Panels within panels
    JButton start, pause, restart, exit;
    JLabel p1, p2, title, instructions;
    JTextField principleValue, minChar;


    public BoggleGUI() {

        setSize(600, 600);
        setName("BOGGLE!");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        upper = new JPanel(new FlowLayout());
        instructions = new JLabel("Welcome to Boggle! The goal of the game is to find more and longer words than the other player!");
        upper.add(instructions);

        middle = new JPanel(new FlowLayout());

        buttons = new JPanel(new GridLayout(2, 2));
        start = new JButton("Start");
        buttons.add(start);
        pause = new JButton("Pause");
        buttons.add(pause);
        restart = new JButton("Restart");
        buttons.add(restart);
        exit = new JButton("Exit");
        buttons.add(exit);
        middle.add(buttons);

        principleValue = new JTextField("Target Score");
        middle.add(principleValue);
        minChar = new JTextField("Shortest word allowed");
        middle.add(minChar);



        lower = new JPanel(new GridLayout(5,5));

        grid = new JLabel[5][5];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new JLabel("Test");
            }
        }

        for (JLabel[] labels : grid) {
            for (JLabel label : labels) {
                lower.add(label);
            }
        }
        add(lower);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equalsIgnoreCase("shake up board")) {
            char[][] characterGrid = generateBoard();
            for(int i = 0; i<5; i++) {
                for(int j = 0; j<5; j++) {
                    grid[i][j].setText(characterGrid[i][j].toString());
                }
            }
        }
    }
}
