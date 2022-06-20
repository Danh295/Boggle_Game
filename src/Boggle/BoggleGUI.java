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
    JPanel upper, middle, lower, wordPrompt; //  3 main panels
    JPanel buttons1, buttons2, scoreBoard, mode, textFields; //  Panels within panels
    JPanel scores; //  Panels within the panels within the panels
    GridBagConstraints frameC, frameC1, frameC2, frameC3, frameC4, middleC, middleC2, middleC3, middleC4, c3, c3version2; // Different constraints for each panel

    JLabel title, instructions, wordLabel, targetScore, countdown;
    JLabel p1, p2;
    JLabel[][] grid;
    JTextField principleValue, word;
    JButton start, pause, restart, exit, ok, pass, shuffle, readPlayerNumber; // Variety of buttons
    JComboBox<String> numberOfPlayers, difficulty; // dropdown menus

    Timer timer;
    TimerTask task;
    int timerCounter; // Time the timer starts at

    public static Human player1;
    public static Human player2;
    public static Computer comp;

    Random rand = new Random();

    public static int tournamentScore;
    int difficultyNumber;
    int countUntilPass = 0;
    int target = 0;
    boolean isTwoPlayers;
    boolean doesP1Start = true;
    boolean isTimerRunning;

    public BoggleGUI() {

        // Initialize class properties
        setSize(1200, 670);
        setName("BOGGLE!");
        setLayout(new GridBagLayout());

        upper = new JPanel();
        upper.setLayout(new GridLayout(3, 1));

        frameC4 = new GridBagConstraints(); // Constraints for GridBag layout
        frameC4.gridx = 0;  // buttons2 panel x-axis position
        frameC4.gridy = 0;  // buttons2 panel y-axis position
        frameC4.ipady = 30; // height in pixels
        frameC = new GridBagConstraints(); // Different constraints for upper panel

        restart = new JButton("Restart");
        restart.addActionListener(this);

        exit = new JButton("Exit");
        exit.addActionListener(this);

        buttons2 = new JPanel(new GridLayout(1, 2)); // Buttons2 panel, first component to be added
        buttons2.add(restart); // Add button to buttons2 panel
        buttons2.add(exit); // Add button to buttons2 panel

        add(buttons2, frameC4); // Add the panel to the frame with the above constraints

        // Prompts
        instructions = new JLabel("Welcome to Boggle! The goal of the game is to find more and longer words than the other player!" +
                                       "\nPlease select difficulty level, number of players, and your target score. ");
        upper.add(instructions);

        // Timer
        timerCounter = 15;
        countdown = new JLabel("Time remaining for turn: " + timerCounter + "s");

        //upper.add(countdown); D: why is this commented out?

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (timerCounter > 0&&isTimerRunning) {
                    timerCounter--;  // Counter goes down by 1
                    countdown.setText("Time remaining for turn: " + timerCounter + "s"); // Label to display time
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);   // Changes every 1000 milliseconds - 1 second

        frameC.ipady = 150; // Upper panel height in pixels
        frameC.gridwidth = 1;   // How many cells it takes up horizontally
        frameC.gridx = 1;   // X-axis position on the frame
        frameC.gridy = 0;   // Y-axis position on the frame

        add(upper, frameC); // Add the upper panel with the above constraints

        // Word Entering Place
        wordPrompt = new JPanel(new FlowLayout());  // All components will be beside each other
        frameC1 = new GridBagConstraints(); // Constraints for this panel as well

        wordLabel = new JLabel("Your word: "); // General label prompting the user
        wordPrompt.add(wordLabel); // Adding label to the panel

        word = new JTextField("Enter word"); // Text field so player can enter the word they found
        wordPrompt.add(word); // Adding the text field to the panel

        ok = new JButton("OK"); // Ok button for when user inputted word
        wordPrompt.add(ok); // Adding the ok button
        ok.addActionListener(this); // Add action listener to the button

        pass = new JButton("Pass Turn"); // If user cannot find a word, they can pass
        wordPrompt.add(pass); // Add the button
        pass.addActionListener(this); // Add action listener to the button

        shuffle = new JButton("Shuffle Board"); // If after 2 rounds of players not being to go, this will button will show up, so the board can be shuffled
        wordPrompt.add(shuffle); // Add the shuffle button
        shuffle.addActionListener(this); // Add action listener to button

        frameC1.gridx = 1; // x-axis position of the panel
        frameC1.gridy = 1; // y-axis position of thr panel
        frameC1.ipady = 20; // How tall the panel is in pixels
        frameC1.ipadx = 250; // How wide the panel is in pixels

        //  Middle Panel
        middle = new JPanel();


        middle.setLayout(new GridBagLayout()); // Gridbaglayout for this panel
        middleC = new GridBagConstraints();  // Constraints for Panel

        // Scoreboard
        scoreBoard = new JPanel(new GridBagLayout());   // JPanel for keeping score chart
        c3 = new GridBagConstraints();

        title = new JLabel("Score Board");
        c3.gridwidth = 2;
        c3.ipady = 10;
        c3.gridx = 0;
        c3.gridy = 0;
        scoreBoard.add(title, c3);

        c3version2 = new GridBagConstraints();
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
        //middle.add(scoreBoard, middleC);

        //  Single or Multiplayer Mode
        mode = new JPanel(new FlowLayout());
        middleC2 = new GridBagConstraints();

        String[] choice = {"Single player", "Multiplayer"};
        numberOfPlayers = new JComboBox<> (choice);
        mode.add(numberOfPlayers);

        readPlayerNumber = new JButton("Submit Mode");
        readPlayerNumber.addActionListener(this);

        mode.add(readPlayerNumber);
        difficulty = new JComboBox<> (new String[]{"Easy", "Hard"});
        //mode.add(difficulty);

        middleC2.gridx = 0;
        middleC2.gridy = 1;
        middleC2.ipady = 15;
        middle.add(mode, middleC2);

        // Text fields for user inputs that set the parameters of the game
        textFields = new JPanel(new FlowLayout());
        middleC3 = new GridBagConstraints();
        targetScore = new JLabel("Target Score");
        principleValue = new JTextField();
        principleValue.setColumns(3);
        textFields.add(targetScore);
        textFields.add(principleValue);
        // minChar = new JTextField("Shortest word allowed");
        // textFields.add(minChar);

        middleC3.gridx = 0;
        middleC3.gridy = 2;
        middleC2.ipady = 15;
        middle.add(textFields, middleC3);

        // A panel within the middle panel for buttons asking if you want to start, exit, etc.
        buttons1 = new JPanel(new GridLayout(1, 2));

        middleC4 = new GridBagConstraints();

        start = new JButton("Start");
        start.addActionListener(this);
        buttons1.add(start);
        pause = new JButton("Pause");
        pause.addActionListener(this);
        buttons1.add(pause);

        middleC4.gridx = 0;
        middleC4.gridy = 3;
        middleC4.ipady = 30;
        middle.add(buttons1, middleC4);

        frameC2 = new GridBagConstraints();  // Constraints for Frame
        frameC2.ipady = 450;
        frameC2.ipadx = 300;
        frameC2.gridwidth = 1;
        frameC2.weighty = 0.5;
        frameC2.gridx = 0;
        frameC2.gridy = 2;
        add(middle, frameC2);

        // Panel to contain the actual grid
        frameC3 = new GridBagConstraints();

        lower = new JPanel(new GridLayout(5,5));
        generateBoard();
        grid = new JLabel[5][5];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new JLabel("" + board[i][j]);
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
        //add(lower, frameC3);

        setVisible(true);
    }
    //K: This was mainly for testing, won't need this
    public static void main(String[] args) {
        BoggleGUI game = new BoggleGUI();
    }
    @Override
    public void actionPerformed(ActionEvent event) {
        String difficultyLevel = difficulty.getSelectedItem().toString();
        String playerNumber = numberOfPlayers.getSelectedItem().toString();
        String command = event.getActionCommand();
        if (command.equals("Submit Mode")) {
            if (playerNumber.equals("Multiplayer")) {
                mode.add(difficulty);
                mode.paintImmediately(0, 0, mode.getWidth(), mode.getHeight());
                mode.revalidate();
            }
            mode.remove(readPlayerNumber);
        }
        if (timerCounter == 0) {
            doesP1Start = !doesP1Start;

        }
        if (command.equals("Submit Difficulty")) {
            mode.remove(readPlayerNumber);
            if (playerNumber.equals("Multiplayer")) {
                mode.add(difficulty);
            }
            mode.remove(numberOfPlayers);
            mode.paintImmediately(0, 0, mode.getWidth(), mode.getHeight());
            mode.revalidate();
        }
        if (playerNumber.equals("Single Player")) {
            isTwoPlayers = false;
            doesP1Start = true;
            p2.setText("Computer" + 0 + "");
            scores.paintImmediately(0, 0, scores.getWidth(), scores.getHeight());
            scoreBoard.paintImmediately(0, 0, scoreBoard.getWidth(), scoreBoard.getHeight());
            middle.paintImmediately(0, 0, middle.getWidth(), middle.getHeight());
            Human player1 = new Human("Player 1", 0);
            comp = new Computer("Computer", 0, difficultyNumber);
        } else if (playerNumber.equals("Multiplayer")) {
            isTwoPlayers = true;
            doesP1Start = rand.nextBoolean();
            player1 = new Human("Player 1", 0);
            player2 = new Human("Player 2", 0);
        }
        if (command.equals("Start")) {
            target = Integer.getInteger(principleValue.getText());
            textFields.remove(principleValue);
            middle.remove(mode);
            upper.remove(instructions);
            upper.add(countdown);
            middle.remove(start);
            buttons1.add(start);
            targetScore.setText("Target Score: " + principleValue.getText());
            tournamentScore = Integer.parseInt(principleValue.getText());
            buttons1.add(pause);
            buttons2.add(exit);
            buttons2.add(restart);
            start.setText("Resume");
            middle.remove(principleValue);
            add(wordPrompt, frameC1);
            middle.add(word);
            middle.add(ok);
            middle.add(scoreBoard, middleC);
            isTimerRunning = true;
            middle.add(scoreBoard);
            upper.add(countdown);
            add(lower, frameC3);
            buttons1.paintImmediately(0, 0, buttons1.getWidth(), buttons1.getHeight());
            buttons1.paintImmediately(0, 0, buttons2.getWidth(), buttons2.getHeight());
            middle.paintImmediately(0, 0, middle.getWidth(), middle.getHeight());
            revalidate();
        } else if (command.equals("Pass")) {
            doesP1Start = !doesP1Start;
            changeColour();
            countUntilPass++;
            if (countUntilPass == 2) {
                generateBoard();
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        grid[i][j].setText("" + board[i][j]);
                    }
                }
            }
        } else if (command.equals("Shuffle Board")) {
            generateBoard();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    grid[i][j].setText("" + board[i][j]);
                }
            }
            /** THe next two functions determine if the user is playing with 1 or 2 players
             * And creates the two players accordingly
             */
            /**
             *
             * Once word is submitted, check to see if it's valid in the board and in the dictionary,
             * then tally points and check if there's a winner
             */
        } else if (command.equals("OK")) {
            countUntilPass = 0;
            int isThereAWinner = 5;
            //boolean b1 = verifyWord_Board(word.getText());
            //boolean b2 = verifyWord_Dict(word.getText(), 0, 109583);
            if (isTwoPlayers) {
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
                    doesP1Start = !doesP1Start;
                    changeColour();
                } else {
                    if (doesP1Start) {
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
                            doesP1Start = !doesP1Start;
                            changeColour();
                        }
                    }
                    if (isThereAWinner == 1) {
                        //Player 1 Wins
                        System.out.println("Player 1 Wins");
                    } else if (isThereAWinner == 2) {
                        if (isTwoPlayers) System.out.println("Player 2 wins. ");
                        else System.out.println("Computer wins. ");
                    }
                }
                word.setText("");
                wordLabel.setText("Your Word: " + word.getText());
            } else if (command.equals("Pause")) {
                isTimerRunning = false;
                wordPrompt.remove(word);
                wordPrompt.remove(ok);
                //wordPrompt.hide();
                wordPrompt.paintImmediately(0, 0, wordPrompt.getWidth(), wordPrompt.getHeight());
                wordPrompt.revalidate();
                repaint();
                revalidate();
            } else if (command.equals("Resume")) {
                isTimerRunning = true;
                wordPrompt.add(word);
                wordPrompt.add(ok);
                wordPrompt.paintImmediately(0, 0, wordPrompt.getWidth(), wordPrompt.getHeight());
                wordPrompt.revalidate();
                repaint();
                revalidate();
            } else if (command.equals("Restart")) {
                reset();
                generateBoard();
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        grid[i][j].setText("" + board[i][j]);
                    }
                }
                timerCounter = 15;
            }
        }
    }
    public static void changeColour() {
        if(doesP1Start) {
            p1.setBackground(Color.BLUE);
            p2.setBackground(Color.WHITE);
        }
        else {
            p1.setBackground(Color.WHITE);
            p2.setBackground(Color.BLUE);
        }
    }
}
