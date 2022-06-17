package Boggle;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

import static Boggle.BoggleGame.board;
import static Boggle.BoggleGame.generateBoard;

public class BoggleGUI extends JFrame implements ActionListener { // D: thinking about inheriting from BoggleGame class, will have to reimplement GUI frame if so
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
    JTextField principleValue, minChar, word;
    JLabel countdown;
    int counter;
    TimerTask task;
    Human player1;
    Human player2;
    Computer comp;
    boolean isTwoPlayers;
    boolean doesP1Start;
    boolean isTimerRunning;
    int difficultyLevel = 0;
    Random rand = new Random();

    public BoggleGUI() {

        setSize(600, 600);
        setName("BOGGLE!");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        upper = new JPanel(new FlowLayout());
        instructions = new JLabel("Welcome to Boggle! The goal of the game is to find more and longer words than the other player!");
        upper.add(instructions);

        // Timer
        counter = 15;
        countdown = new JLabel("Time remaining: " + String.valueOf(counter) + "s");
        upper.add(countdown);

        Timer timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (counter > 0&&isTimerRunning) {
                    counter--;
                    countdown.setText("Time remaining: " + String.valueOf(counter) + "s");
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);

        //Middle Panel
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

        // Word Textfield
        word = new JTextField("Input word");
        middle.add(word);
        add(middle);


        // Panel to contain the actual grid
        lower = new JPanel(new GridLayout(5, 5));

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

        if (command.equals("shake up board")) {
            BoggleGame.generateBoard();
            /** THe next two functions determine if the user is playing with 1 or 2 players
             * And creates the two players accordingly
             */
        } else if (command.equals("Multiplayer")) {
            isTwoPlayers = true;
            remove(multi);
            remove(single);
            doesP1Start = true;
            Human player1 = new Human("Player 1", 0);
            comp = new Computer("Computer", 0, difficultyLevel);
        } else if (command.equals("Single Player")) {
            isTwoPlayers = false;
            remove(multi);
            remove(single);
            doesP1Start = rand.nextBoolean();
            player1 = new Human("Player 1", 0);
            player2 = new Human("Player 2", 0);
        }
        /**
         *
         * Once word is submitted, check to see if it's valid in the board and in the dictionary,
         * then tally points and check if there's a winner
         */
        else if (command.equals("Submit Word")) {
            int isThereAWinner = 5;
            if (BoggleGame.verifyWord_Board(word.getText()) && BoggleGame.verifyWord_Dict(word.getText(), 0, 109583)) {
                if (isTwoPlayers) {
                    if (doesP1Start) {
                        player1.addScore(word.getText());
                    } else {
                        //player2.tallyPoints(start);
                    }
                    isThereAWinner = BoggleGame.isWinner(player1, player2);

                } else {
                    if (doesP1Start) {
                        player1.addScore(word.getText());
                    } else {
                        comp.addScore(word.getText());
                    }
                    isThereAWinner = BoggleGame.isWinner(player1, comp);
                }
                doesP1Start= !doesP1Start;
                if (isThereAWinner == 1) {
                    //Player 1 Wins
                    System.out.println("Player 1 Wins");
                } else if (isThereAWinner == 2) {
                    if (isTwoPlayers) System.out.println("Player 2 wins. ");
                    else System.out.println("Computer wins. ");
                }
            }
        } else if (command.equals("Pause")) {
            isTimerRunning = false;
        } else if (command.equals("Resume")) {
            isTimerRunning = true;
        } else if(command.equals("Reset")) {
            BoggleGame.generateBoard();
            player1.setScore(0);
            player2.setScore(0);
        } else if(command.equals("Easy")) {
            difficultyLevel = 1;
        } else if(command.equals("Medium")) {
            difficultyLevel = 2;
        } else if(command.equals("Hard")) {
            difficultyLevel = 3;
        }
    }
}
