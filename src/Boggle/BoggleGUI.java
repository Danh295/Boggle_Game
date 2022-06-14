package Boggle;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import static Boggle.BoggleGame.board;

public class BoggleGUI extends JFrame implements ActionListener{

    JLabel[][] grid;
    JPanel upper, middle, lower;    //  3 main panels
    JPanel buttons, scoreBoard, mode;     //  Panels within panels
    JPanel scores;  //  Panels within the panels within the panels
    JButton start, pause, restart, exit;
    JButton single, multi;
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

        // A panel within the middle panel for buttons asking if you want to start, exit, etc.
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

        // Text fields for user inputs that set the parameters of the game
        principleValue = new JTextField("Target Score");
        middle.add(principleValue);
        minChar = new JTextField("Shortest word allowed");
        middle.add(minChar);

        // Single or multiplayer and if single, choose difficulty
        mode = new JPanel(new GridLayout(2, 2));
        single = new JButton("Single Player");
        mode.add(single);
        multi = new JButton("Multiplayer");
        mode.add(multi);


        // JPanel to make the scoreboard
        JPanel scoreBoard = new JPanel();   // JPanel for keeping score chart
        scoreBoard.setLayout(new BoxLayout(scoreBoard, BoxLayout.Y_AXIS));  // Making it a box layout so it can go from top to bottom
        title = new JLabel("Score Board");
        scoreBoard.add(title);

        JPanel scores = new JPanel(new FlowLayout());   // Flow layout because p1 and p2 scores are going to be beside each other
        p1 = new JLabel("0");
        scores.add(p1);
        p2 = new JLabel("0");
        scores.add(p2);
        scoreBoard.add(scores);

        middle.add(scoreBoard);
        add(middle);


        // Panel to contain the actual grid
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
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if(command.equals("shake up board")) {
//            char[][] characterGrid = generateBoard();
            /* this is danny, i changed the generateBoard method into a void
             * method and just made the game board into a global variable. so
             * instead of needing to create a new one everywhere you can just
             * call it from the class
             */
            for(int i = 0; i<5; i++) {
                for(int j = 0; j<5; j++) {
                    grid[i][j].setText(String.valueOf(board[i][j]));
                }
            }
        }
    }
}
