package Boggle;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

import static Boggle.BoggleGame.*;

public class BoggleGUI extends JFrame implements ActionListener { // D: thinking about inheriting from BoggleGame class, will have to reimplement GUI frame if so
    // it would make sense if we have a new game object each time, instead of resetting everything each time
    // if we do end up creating a new game object each time, it would make sense for the board to be an instance variable, so this class must then extend the game class

    JLabel[][] grid;
    JPanel upper, middle, lower, wordPrompt;    //  3 main panels
    JPanel buttons, scoreBoard, mode, textFields;     //  Panels within panels
    JPanel scores;  //  Panels within the panels within the panels
    JButton start, pause, restart, exit, ok;
    JComboBox numberOfPlayers, difficulty;
    JLabel p1, p2, title, instructions, instructions2, wordLabel;
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
    int difficultyNumber;
    Random rand = new Random();

    public BoggleGUI() {
        /*  // What Kaiden did
        setSize(600, 600);
        setName("BOGGLE!");
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        upper = new JPanel(new FlowLayout());
        instructions = new JLabel("Welcome to Boggle! The goal of the game is to find more and longer words than the other player!");
        instructions2 = new JLabel("Please select difficulty level, number of players, and your target score. ");
        upper.add(instructions);
        upper.add(instructions2);
        // Timer
        counter = 15;
        countdown = new JLabel("Time remaining: " + String.valueOf(counter) + "s");

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
        add(upper);
        //Middle Panel
        middle = new JPanel(new FlowLayout());

        // A panel within the middle panel for buttons asking if you want to start, exit, etc.
        buttons = new JPanel(new GridLayout(2, 2));
        start = new JButton("Start");
        start.addActionListener(this);
        pause = new JButton("Pause");
        pause.addActionListener(this);
        restart = new JButton("Restart");
        restart.addActionListener(this);
        exit = new JButton("Exit");
        exit.addActionListener(this);
        middle.add(buttons);

        // Text fields for user inputs that set the parameters of the game
        principleValue = new JTextField("Target Score");
        //middle.add(principleValue);
        //K: Don't think we need this, default min length is 3.
        //minChar = new JTextField("Shortest word allowed");
        //middle.add(minChar);

        // Single or multiplayer and if single, choose difficulty
        mode = new JPanel(new GridLayout(2, 2));
        /*single = new JButton("Single Player");
        mode.add(single);
        single.addActionListener(this);
        multi = new JButton("Multiplayer");
        mode.add(multi);
        multi.addActionListener(this);
        */
        /*
        String[] options = new String[]{"Single Player", "Multiplayer"};
        numberOfPlayers = new JComboBox(options);
        // Dropdown menu
        String[] choices = {"Easy", "Medium", "Hard"};
        difficulty = new JComboBox(choices);

        mode.add(difficulty);
        mode.add(numberOfPlayers);
        middle.add(mode);
        middle.add(principleValue);
        middle.add(start);
        // JPanel to make the scoreboard
        scoreBoard = new JPanel();   // JPanel for keeping score chart
        scoreBoard.setLayout(new BoxLayout(scoreBoard, BoxLayout.Y_AXIS));  // Making it a box layout so it can go from top to bottom
        title = new JLabel("Score Board");
        scoreBoard.add(title);

        JPanel scores = new JPanel(new FlowLayout());   // Flow layout because p1 and p2 scores are going to be beside each other
        p1 = new JLabel("0");
        scores.add(p1);
        p2 = new JLabel("0");
        scores.add(p2);
        scoreBoard.add(scores);

        //middle.add(scoreBoard);

        // Word Textfield
        word = new JTextField("Input word");
        //middle.add(word);
        add(middle);

        submit = new JButton("Submit Word");
        submit.addActionListener(this);
        */

        setSize(1100, 670);
        setName("BOGGLE!");
        setLayout(new GridBagLayout());

        GridBagConstraints frameC = new GridBagConstraints();

        upper = new JPanel();
        upper.setLayout(new GridLayout(3, 1));
        instructions = new JLabel("Welcome to Boggle! The goal of the game is to find more and longer words than the other player!");
        instructions2 = new JLabel("Please select difficulty level, number of players, and your target score. ");
        upper.add(instructions);
        upper.add(instructions2);

        // Timer
        counter = 15;
        countdown = new JLabel("Time remaining for turn: " + String.valueOf(counter) + "s");
        upper.add(countdown);

        Timer timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (counter > 0&&isTimerRunning) {
                    counter--;
                    countdown.setText("Time remaining for turn: " + String.valueOf(counter) + "s");
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);

        frameC.ipady = 150;
        frameC.ipadx = 400;
        frameC.gridwidth = 2;
        frameC.gridx = 0;
        frameC.gridy = 0;
        add(upper, frameC);

        // Word Entering Place
        wordPrompt = new JPanel(new FlowLayout());
        GridBagConstraints frameC1 = new GridBagConstraints();

        wordLabel = new JLabel("Your word: ");
        wordPrompt.add(wordLabel);
        word = new JTextField("Enter word");
        wordPrompt.add(word);
        ok = new JButton("OK");
        wordPrompt.add(ok);
        ok.addActionListener(this);

        frameC1.gridx = 1;
        frameC1.gridy = 1;
        frameC1.ipady = 20;
        frameC1.ipadx = 250;
        add(wordPrompt, frameC1);

        //  Middle Panel
        middle = new JPanel();
        GridBagConstraints frameC2 = new GridBagConstraints();  // Constraints for Frame

        middle.setLayout(new GridBagLayout());
        GridBagConstraints middleC = new GridBagConstraints();  // Constraints for Panel

        // JPanel to make the scoreboard
        scoreBoard = new JPanel(new GridBagLayout());   // JPanel for keeping score chart
        GridBagConstraints c3 = new GridBagConstraints();

        title = new JLabel("Score Board");
        c3.gridwidth = 2;
        c3.ipady = 10;
        c3.gridx = 0;
        c3.gridy = 0;
        scoreBoard.add(title, c3);

        GridBagConstraints c3version2 = new GridBagConstraints();
        scores = new JPanel(new FlowLayout());

        Border border = BorderFactory.createLineBorder(Color.BLACK);

        p1 = new JLabel(" Player 1: " + 0 + " ");
        p1.setBorder(border);
        scores.add(p1);

        p2 = new JLabel(" Player 2: " + 0 + " ");
        p2.setBorder(border);
        scores.add(p2);

        c3version2.gridx = 0;
        c3version2.gridy = 1;
        c3version2.ipady = 15;
        scoreBoard.add(scores, c3version2);

        middleC.gridx = 0;
        middleC.gridy = 0;
        middle.add(scoreBoard, middleC);

        //  Single or Multiplayer Mode
        mode = new JPanel(new FlowLayout());
        GridBagConstraints middleC2 = new GridBagConstraints();
        String[] choice = {"Single player", "Multiplayer"};
        numberOfPlayers = new JComboBox(choice);
        mode.add(numberOfPlayers);
        String[] choices = {"Easy", "Medium", "Hard"};
        difficulty = new JComboBox(choices);
        mode.add(difficulty);

        middleC2.gridx = 0;
        middleC2.gridy = 1;
        middleC2.ipady = 15;
        middle.add(mode, middleC2);

        // Text fields for user inputs that set the parameters of the game
        textFields = new JPanel(new FlowLayout());
        GridBagConstraints middleC3 = new GridBagConstraints();

        principleValue = new JTextField("Target Score");
        textFields.add(principleValue);
        minChar = new JTextField("Shortest word allowed");
        textFields.add(minChar);

        middleC3.gridx = 0;
        middleC3.gridy = 2;
        middleC2.ipady = 15;
        middle.add(textFields, middleC3);

        // A panel within the middle panel for buttons asking if you want to start, exit, etc.
        buttons = new JPanel(new GridLayout(2, 2));

        GridBagConstraints middleC4 = new GridBagConstraints();

        start = new JButton("Start");
        start.addActionListener(this);
        buttons.add(start);
        pause = new JButton("Pause");
        pause.addActionListener(this);
        buttons.add(pause);
        restart = new JButton("Restart");
        restart.addActionListener(this);
        buttons.add(restart);
        exit = new JButton("Exit");
        exit.addActionListener(this);
        buttons.add(exit);

        middleC4.gridx = 0;
        middleC4.gridy = 3;
        middleC4.ipady = 30;
        middle.add(buttons, middleC4);

        frameC2.ipady = 450;
        frameC2.ipadx = 300;
        frameC2.gridwidth = 1;
        frameC2.weighty = 0.5;
        frameC2.gridx = 0;
        frameC2.gridy = 2;
        add(middle, frameC2);

        // Panel to contain the actual grid
        GridBagConstraints frameC3 = new GridBagConstraints();

        lower = new JPanel(new GridLayout(5,5));
        generateBoard();
        grid = new JLabel[5][5];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new JLabel("Test");
                grid[i][j].setText("" + board[i][j]);
            }
        }

        for (JLabel[] labels : grid) {
            for (JLabel label : labels) {
                lower.add(label);
            }
        }

        frameC3.ipady = 350;
        frameC3.ipadx = 300;
        frameC3.gridwidth = 1;
        frameC3.weighty = 0.5;
        frameC3.gridx = 1;
        frameC3.gridy = 2;
        add(lower, frameC3);

        setVisible(true);


        /*  // What Kaiden did
        // Panel to contain the actual grid
        lower = new JPanel(new GridLayout(5, 5));
        generateBoard();
        grid = new JLabel[5][5];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new JLabel("Test");
                grid[i][j].setText("" + board[i][j]);
            }
        }

        for (JLabel[] labels : grid) {
            for (JLabel label : labels) {
                lower.add(label);
            }
        }
        //add(lower);

        setVisible(true);
         */
    }

    public static void main(String[] args) {
        BoggleGUI game = new BoggleGUI();
    }
    @Override
    public void actionPerformed(ActionEvent event) {
        String difficultyLevel = difficulty.getSelectedItem().toString();
        String playerNumber = numberOfPlayers.getSelectedItem().toString();
        if(difficultyLevel.equals("Easy")) {
            difficultyNumber = 1;
        }
        else if(difficultyLevel.equals("Medium")) {
            difficultyNumber = 2;
        }
        else if(difficultyLevel.equals("Hard")) {
            difficultyNumber = 3;
        }
        if(playerNumber.equals("Single Player")) {
            isTwoPlayers = false;
            doesP1Start = true;
            Human player1 = new Human("Player 1", 0);
            comp = new Computer("Computer", 0, difficultyNumber);
        }
        else if(playerNumber.equals("Multiplayer")) {
            isTwoPlayers = true;
            doesP1Start = rand.nextBoolean();
            player1 = new Human("Player 1", 0);
            player2 = new Human("Player 2", 0);
        }
        middle.remove(mode);
        String command = event.getActionCommand();
        middle.remove(start);
        buttons.add(start);
        buttons.add(pause);
        buttons.add(exit);
        buttons.add(restart);
        start.setText("Resume");
        middle.remove(principleValue);
        middle.add(word);
        middle.add(ok);
        isTimerRunning = true;
        middle.add(scoreBoard);
        upper.add(countdown);
        add(lower);
        buttons.paintImmediately(0,0,buttons.getWidth(), buttons.getHeight());
        middle.paintImmediately(0,0,middle.getWidth(), middle.getHeight());
        revalidate();
        if (command.equals("shake up board")) {
            BoggleGame.generateBoard();
            /** THe next two functions determine if the user is playing with 1 or 2 players
             * And creates the two players accordingly
             */
            /**
             *
             * Once word is submitted, check to see if it's valid in the board and in the dictionary,
             * then tally points and check if there's a winner
             */
        }
        else if (command.equals("Submit Word")) {
            int isThereAWinner = 5;
            boolean b1 = BoggleGame.verifyWord_Board(word.getText());
            boolean b2 = BoggleGame.verifyWord_Dict(word.getText(), 0, 109583);
            if (BoggleGame.verifyWord_Board(word.getText()) && BoggleGame.verifyWord_Dict(word.getText(), 0, 109583)) {
                if (isTwoPlayers) {
                    if (doesP1Start) {
                        player1.addScore(word.getText());
                    } else {
                        player2.addScore(word.getText());
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
        }
    }
}
