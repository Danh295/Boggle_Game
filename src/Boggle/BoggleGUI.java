package Boggle;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

import static Boggle.BoggleGame.board;
import static Boggle.BoggleGame.generateBoard;

public class BoggleGUI extends JFrame implements ActionListener{ // D: thinking about inheriting from BoggleGame class, will have to reimplement GUI frame if so
                                                                 // it would make sense if we have a new game object each time, instead of resetting everything each time
                                                                 // if we do end up creating a new game object each time, it would make sense for the board to be an instance variable, so this class must then extend the game class

    JLabel[][] grid;
    JPanel upper, middle, lower;    //  3 main panels
    JPanel buttons, scoreBoard, mode;     //  Panels within panels
    JPanel scores;  //  Panels within the panels within the panels
    JButton start, pause, restart, exit;
    JButton single, multi;
    JComboBox difficulty;
    JLabel p1, p2, title, instructions;
    JTextField principleValue, minChar;
    JLabel timer;
    int counter;
    TimerTask task;

    boolean isTwoPlayers;
    boolean doesP1Start;
    boolean isTimerRunning;
    Random rand = new Random();
    public BoggleGUI() {

        setSize(600, 600);
        setName("BOGGLE!");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        upper = new JPanel(new FlowLayout());
        instructions = new JLabel("Welcome to Boggle! The goal of the game is to find more and longer words than the other player!");
        upper.add(instructions);

        // Timer




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
        // Dropdown menu
        String[] choices = {"Easy", "Medium", "Hard"};
        JComboBox difficulty = new JComboBox(choices);

        mode.add(difficulty);
        middle.add(mode);

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
            /* D: i changed the generateBoard method into a void
             * method and just made the game board into a global variable. so
             * instead of needing to create a new one everywhere you can just
             * call it from the class
             */
            generateBoard();
            for(int i = 0; i<5; i++) {
                for(int j = 0; j<5; j++) {
                    grid[i][j].setText(String.valueOf(board[i][j]));
                }
            }
        }
        else if(command.equals("Multiplayer")) {
            isTwoPlayers = true;
            remove(multi);
            remove(single);
            doesP1Start = true;
        }
        else if(command.equals("Single Player")) {
            isTwoPlayers = false;
            remove(multi);
            remove(single);
            doesP1Start = rand.nextBoolean();
        }
        /**
         *
         * Once word is submitted, check to see if it's valid in the board and in the dictionary,
         * then tally points and check if there's a winner
         */
        else if(command.equals("Submit Word")) {
            int isThereAWinner = 5;
            if (BoggleGame.verifyWord_Board("Start") && BoggleGame.verifyWord_Dict("Start", 0, 109583)) {
                if(isTwoPlayers) {
                    if(doesP1Start) {
                        //player1.tallyPoints(start);
                    }
                    else {
                        //player2.tallyPoints(start);
                    }
                    //isThereAWinner = BoggleGame.isWinner(player1, player2)

                }
                else {
                    if (doesP1Start) {
                        //player1.tallyPoints(start);
                    } else {
                        //computer.tallyPoints(start);
                    }
                    //isThereAWinner = BoggleGame.isWinner(player1, computer)
                }
                if(isThereAWinner == 1) {
                    //Player 1 Wins
                    System.out.println("Player 1 Wins");
                }
                else if(isThereAWinner == 2) {
                    if(isTwoPlayers) System.out.println("Player 2 wins. ");
                    else System.out.println("Computer wins. ");
                }
            }
        }
        else if(command.equals("Pause")) {
            isTimerRunning = false;
            while(!isTimerRunning) {
                //pause timer
            }
        }
    }
}
