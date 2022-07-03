package Boggle;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

import static Boggle.BoggleGame.*;

/* todo:
 * - format exit & restart buttons to top left
 * - make 'pass turn' button functional (switch turns when clicked)
 *      - increment counter; when both players pass twice, show 'shuffle board' button
 * - when timer reaches 0, automatically submits current entered word if there is anything there
 *      - sysMessage: warning starting from the 5 second mark - "player x 's turn is ending soon, submit your word"
 *      - restart timer & switch turn to other player
 * - when a word is submitted, bring timer to 0 & switch turn
 *      - sysMesdsage - turn swticehs
 * - sysMessage: show current player turn
 * - Move 'Your word' JLabel in front of the text field
 * - make 'pass turn' button functional (switch turns when clicked)
 *      - increment counter; when both players pass twice, show 'shuffle board' button
 * - when timer reaches 0, automatically submits current word & switches turn
 *      - sysMessage: warning starting from the 5 second mark
 * - sysMessage: show current player turn
 * - 'Your word' JLabel:
 * - edit the ok button to adapt to one/two players
 *
 */

public class BoggleGUI extends JFrame implements ActionListener {

    // it would make sense if we have a new game object each time, instead of resetting everything each time
    // if we do end up creating a new game object each time, it would make sense for the board to be an instance variable, so this class must then extend the game class
    JPanel upper, left, right, wordPrompt, lower; //  3 main panels
    JPanel buttons1, buttons2, scoreBoard, mode, textFields; //  Panels within panels
    JPanel scores; //  Panels within the panels within the panels
    GridBagConstraints frameC, frameC1, frameC2, frameC3, frameC4, leftC, leftC2, leftC3, leftC4, c3, c3version2; // Different constraints for each panel

    JLabel title, instructions1, instructions2, sysMessage, wordLabel, targetScore, countdown, lab1, lab2;
    JLabel p1, p2;
    JLabel[][] grid;
    JTextField principleValue, word, p1Name, p2Name;
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
    static int difficultyNumber;
    static int countUntilPass = 0;
    int target = 0;
    int isThereAWinner = 5;
    static boolean isTwoPlayers = true;
    static boolean p1Turn = true;
    boolean isTimerRunning = false;

    public BoggleGUI() {

        player1 = new Human("Player 1", 0);
        player2 = new Human("Player 2", 0);
        comp = new Computer("Computer", 0, 0);
        // Initialize class properties
        setSize(1200, 670);
        setName("BOGGLE!");
        setLayout(new GridBagLayout());

        buttons2 = new JPanel(new GridLayout(1, 2)); // Buttons2 panel, first component to be added

        restart = new JButton("Restart");
        restart.addActionListener(this);

        exit = new JButton("Exit");
        exit.addActionListener(this);

        buttons2.add(restart); // Add button to buttons2 panel
        buttons2.add(exit); // Add button to buttons2 panel

        frameC4 = new GridBagConstraints(); // Constraints for GridBag layout
        frameC4.gridx = 0;  // buttons2 panel x-axis position
        frameC4.gridy = 0;  // buttons2 panel y-axis position
        frameC4.ipady = 30; // height in pixels
        frameC4.fill = GridBagConstraints.HORIZONTAL;

        add(buttons2, frameC4); // Add the panel to the frame with the above constraints

        //buttons2.hide();

        buttons2.setVisible(false);

        upper = new JPanel();
        upper.setLayout(new GridLayout(3, 1));
        frameC = new GridBagConstraints(); // Different constraints for upper panel

        // Instructions & messages
        instructions1 = new JLabel("Welcome to Boggle! The goal of the game is to find more and longer words than the other player!");
        instructions2 = new JLabel("Please select difficulty level, number of players, and your target score.");
        sysMessage = new JLabel("");
        upper.add(instructions1);
        upper.add(instructions2);
        upper.add(sysMessage);

        // Timer
        timerCounter = 15;
        countdown = new JLabel("Time remaining for turn: " + timerCounter + "s");

        //upper.add(countdown); D: why is this commented out?

        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (timerCounter > 0 && isTimerRunning) {
                    timerCounter--;  // Counter goes down by 1
                    countdown.setText("Time remaining for turn: " + timerCounter + "s"); // Label to display time
                    if (timerCounter == 5) {
                        sysMessage.setText("5 Seconds Left!");
                        countdown.setForeground(Color.RED);
                        countdown.setOpaque(true);
                        countdown.paintImmediately(0,0,countdown.getWidth(), countdown.getHeight());
                    }
                }
                else if (timerCounter == 0) {
                    p1Turn = !p1Turn;
                    sysMessage.setText("Time's Up! Onto the next player. ");
                    changeColour();
                    timerCounter = 15;
                }
                else {
                    countdown.setForeground(Color.BLACK);
                    countdown.setOpaque(true);
                    countdown.paintImmediately(0,0,countdown.getWidth(), countdown.getHeight());
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

        wordLabel = new JLabel("Last word: "); // General label prompting the user
        wordPrompt.add(wordLabel); // Adding label to the panel

        word = new JTextField("Enter word"); // Text field so player can enter the word they found
        wordPrompt.add(word); // Adding the text field to the panel
        word.setColumns(10);    
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
        left = new JPanel();

        left.setLayout(new GridBagLayout()); // Gridbaglayout for this panel
        leftC = new GridBagConstraints();  // Constraints for Panel

        // Scoreboard
        scoreBoard = new JPanel(new GridBagLayout());   // JPanel for keeping score chart
        c3 = new GridBagConstraints();

        title = new JLabel("Score Board");
        c3.gridwidth = 2; // 2 cells wide
        c3.ipady = 10; // 10 pixels tall
        c3.gridx = 0; // x-axis position
        c3.gridy = 0; // y-axis position
        scoreBoard.add(title, c3); // Add it with constraints

        c3version2 = new GridBagConstraints(); // Constraints for bottom part of table
        scores = new JPanel(new FlowLayout());

        Border border = BorderFactory.createLineBorder(Color.BLACK); // Making a black border for the scores

        p1 = new JLabel(" Player 1: " + 0 + " "); // Player 1 score label
        p1.setBorder(border); // Add the border to the label
        p1.setOpaque(true);
        scores.add(p1); // Add label

        p2 = new JLabel(" Player 2: " + 0 + " "); // Player 2 score label
        p2.setBorder(border); // Add border to label
        p2.setOpaque(true);
        scores.add(p2); // Add label

        c3version2.gridx = 0; // x-axis position
        c3version2.gridy = 1; // y-axis position
        c3version2.ipady = 15; // Height in pixels
        scoreBoard.add(scores, c3version2); // Add panel to score board panel with constraints

        leftC.gridx = 0; // x-axis position for score board when it is added
        leftC.gridy = 0; // y-axis position for score board when it is added

        //  Single or Multiplayer Mode
        mode = new JPanel(new FlowLayout()); // Flow layout, just want things beside each other
        leftC2 = new GridBagConstraints();

        String[] choice = {"Single player", "Multiplayer"}; // 2 choices of modes
        numberOfPlayers = new JComboBox<> (choice); // Make the dropdown menu with the choices
        mode.add(numberOfPlayers); // Add dropdown menu

        readPlayerNumber = new JButton("Submit Mode"); // Submit the mode
        readPlayerNumber.addActionListener(this);
        mode.add(readPlayerNumber); // Add the button

        difficulty = new JComboBox<> (new String[]{"Easy", "Hard"}); // Difficulty dropdown menu

        leftC2.gridx = 0; // x-axis position
        leftC2.gridy = 1; // y-axis position
        leftC2.ipady = 15; // Height in pixels
        left.add(mode, leftC2); // Add the panel with the constraints to the middle panel

        // Text fields for user inputs that set the parameters of the game
        textFields = new JPanel(new FlowLayout()); // Want components beside each other
        leftC3 = new GridBagConstraints(); // New constraints
        targetScore = new JLabel("Target Score"); // Label prompting target score
        principleValue = new JTextField(); // Text field to input score
        principleValue.setColumns(3); // Changing size of text field
        textFields.add(targetScore); // Add the label
        textFields.add(principleValue); // Add the text field
        // minChar = new JTextField("Shortest word allowed");
        // textFields.add(minChar);

        leftC3.gridx = 0; // x-axis position
        leftC3.gridy = 2; // y-axis position
        leftC2.ipady = 15; // Height in pixels
        left.add(textFields, leftC3); // Add

        // A panel within the middle panel for buttons asking if you want to start, exit, etc.
        buttons1 = new JPanel(new GridLayout(1, 2));

        leftC4 = new GridBagConstraints();

        start = new JButton("Start"); //start button
        start.addActionListener(this);
        buttons1.add(start); // add start to button
        pause = new JButton("Pause");//pause button
        pause.addActionListener(this);
        buttons1.add(pause); //add pause to buttons
        pause.setVisible(false);

        //buttons1.hide();

        start.setVisible(false);

        leftC4.gridx = 0; // x-axis position
        leftC4.gridy = 3; // y-axis position
        leftC4.ipady = 30; // height in pixels
        left.add(buttons1, leftC4); // Add with constraints

        frameC2 = new GridBagConstraints();  // Constraints for Frame
        frameC2.ipady = 450; // height in pixels
        frameC2.ipadx = 300; //width in pixels
        frameC2.gridwidth = 1; // how many cells it takes up
        frameC2.weighty = 0.5; // Padding between components in y-axis
        frameC2.gridx = 0; // x-axis position
        frameC2.gridy = 2; // y-axis position
        add(left, frameC2); // Add with constraints

        // Panel to contain the actual grid
        frameC3 = new GridBagConstraints();

        right = new JPanel(new GridLayout(5,5)); // Grid layout for letters
        generateBoard(); // Run the method

        right = new JPanel(new GridLayout(5,5)); // Grid layo
        generateBoard();

        grid = new JLabel[5][5];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new JLabel("" + board[i][j]); // Each label will be taken up by a letter
            }
        }

        for (JLabel[] labels : grid) { // Show the label
            for (JLabel label : labels) {
                right.add(label);
            }
        }

        frameC3.ipady = 350; // height in pixels
        frameC3.ipadx = 300; // width in pixels
        frameC3.gridwidth = 1; // how many cells it takes up
        frameC3.weighty = 0.5; // padding between other components in y-axis
        frameC3.gridx = 1; // x-axis position
        frameC3.gridy = 2; // y-axis position

        lower = new JPanel(new GridLayout(2,2));
        lab1 = new JLabel("Player 1's name: ");
        p1Name = new JTextField();
        lab2 = new JLabel("Player 2's name: ");
        p2Name = new JTextField();
        lower.add(lab1);
        lower.add(p1Name);
        lower.add(lab2);
        lower.add(p2Name);
        lab2.setVisible(false);
        p2Name.setVisible(false);
        mode.add(lower);
        mode.paintImmediately(0,0,mode.getWidth(), mode.getHeight());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String playerNumber = Objects.requireNonNull(numberOfPlayers.getSelectedItem()).toString();
        String command = event.getActionCommand();

        if (command.equals("Submit Mode")) {
            start.setVisible(true);
            textFields.remove(targetScore);
            principleValue.setVisible(false);
            buttons1.paintImmediately(0,0,buttons1.getWidth(), buttons1.getHeight());
            left.paintImmediately(0,0,left.getWidth(), left.getHeight());
            repaint();
            revalidate();
            if (playerNumber.equals("Single player")) {
                mode.add(difficulty);
            }
            else {
                lab2.setVisible(true);
                p2Name.setVisible(true);
            }
            mode.remove(readPlayerNumber);
            mode.remove(numberOfPlayers);
            player1.setName(p1Name.getText());
            p1Name.setVisible(false);
            lab1.setVisible(false);
            mode.paintImmediately(0,0 , mode.getWidth(), mode.getHeight());
            mode.revalidate();
            left.paintImmediately(0,0,left.getWidth(), left.getHeight());
            left.revalidate();
            repaint();
            revalidate();
        }


/*        if (command.equals("Submit Difficulty")) {
            start.setVisible(true);
            buttons1.paintImmediately(0,0,buttons1.getWidth(), buttons1.getHeight());
            mode.remove(readPlayerNumber);
            if (playerNumber.equals("Single player")) {
                mode.add(difficulty);
            }
            mode.remove(numberOfPlayers);

            lower.paintImmediately(0,0,lower.getWidth(), lower.getHeight());
            mode.paintImmediately(0,0 , mode.getWidth(), mode.getHeight());
            mode.revalidate();
//
            changeColour();
            sysMessage.setText("Time's Up! Next player's turn. ");
        }

 */
        if (playerNumber.equals("Single player")) {
            isTwoPlayers = false;
            p1Turn = true;
            p2.setText("Computer" + 0 + "");
            scores.paintImmediately(0, 0, scores.getWidth(), scores.getHeight());
            scoreBoard.paintImmediately(0, 0, scoreBoard.getWidth(), scoreBoard.getHeight());
            left.paintImmediately(0, 0, left.getWidth(), left.getHeight());
            if(difficulty.getSelectedItem().toString().equals("Easy")) {
                difficultyNumber = 1;
            }
            else {
                difficultyNumber = 2;
            }
            comp.changeDifficulty(difficultyNumber);

        } else if (playerNumber.equals("Multiplayer")&&command.equals("Start")) {
            isTwoPlayers = true;
            p1Turn = rand.nextBoolean();
        }

        switch (command) {
            case "Start" -> {
                player2.setName(p2Name.getText());
                mode.remove(lower);
                changeColour();
                target = (Integer.parseInt(principleValue.getText()));
                buttons1.setVisible(true);
                pause.setVisible(true);
                textFields.remove(principleValue);
                left.remove(mode);
                upper.remove(instructions1);
                upper.remove(instructions2);
                upper.add(countdown);
                buttons1.add(start);
                targetScore.setText("Target Score: " + principleValue.getText());
                tournamentScore = Integer.parseInt(principleValue.getText());
                buttons1.add(pause);
                buttons2.add(exit);
                buttons2.add(restart);
                start.setText("Resume");
                left.remove(principleValue);
                add(wordPrompt, frameC1);
                left.add(word);
                left.add(ok);
                left.add(scoreBoard, leftC);
                isTimerRunning = true;
                p1.setText(p1Name.getText() + ":"+ 0);
                if(isTwoPlayers) {
                    p2.setText(p2Name.getText() + ":"+ 0);
                }
                else {
                    p2.setText("Computer"+ ":" + 0);
                }
                left.add(scoreBoard);
                upper.add(countdown);
                add(right, frameC3);
                wordPrompt.remove(word);
                wordPrompt.remove(ok);
                wordPrompt.add(word);
                wordPrompt.add(ok);
                textFields.paintImmediately(0, 0, textFields.getWidth(), textFields.getHeight());
                buttons1.paintImmediately(0, 0, buttons1.getWidth(), buttons1.getHeight());
                buttons2.paintImmediately(0, 0, buttons2.getWidth(), buttons2.getHeight());
                left.paintImmediately(0, 0, left.getWidth(), left.getHeight());
                right.paintImmediately(0,0,right.getWidth(), right.getHeight());
                repaint();
                revalidate();
            }
            case "Pass Turn" -> {
                p1Turn = !p1Turn;
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
            }
            case "Shuffle Board" -> {
                generateBoard();
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        grid[i][j].setText("" + board[i][j]);
                    }
                }
            }
            /* The next two functions determine if the user is playing with 1 or 2 players
             * And creates the two players accordingly
             *
             * Once word is submitted, check to see if it's valid in the board and in the dictionary,
             * then tally points and check if there's a winner
             */
            case "OK" -> {
                countUntilPass = 0;
                int isThereAWinner = 5;
                boolean b1 = verifyWord_Board(word.getText());
                boolean b2 = verifyWord_Dict(word.getText(), 0, 109583);
                    if (verifyWord_Board(word.getText()) && verifyWord_Dict(word.getText(), 0, 109583)) {
                        timerCounter = 15;
                        if (isTwoPlayers) {
                            if (p1Turn) {
                                player1.addScore(word.getText());
                                wordLabel.setText("Last word: " + word.getText());
                            } else {
                                player2.addScore(word.getText());
                            }
                            isThereAWinner = BoggleGame.isWinner(player1, player2);
                            p1.setText(player1.getID() + ":" + player1.getScore());
                            p1.paintImmediately(0, 0, p1.getWidth(), p1.getHeight());
                            p2.setText(player2.getID() + ":" + player2.getScore());
                            p1Turn = !p1Turn;

                        } else {
                            if (p1Turn) {
                                player1.addScore(word.getText());
                                wordLabel.setText("Last word: " + word.getText());
                            } else {
                                comp.addScore(word.getText());
                                wordLabel.setText("Last word: " + word.getText());
                            }
                            isThereAWinner = BoggleGame.isWinner(player1, comp);
// conlfict
                            p1.setText(player1.getID() + ":" + player1.getScore());
                            p1.paintImmediately(0, 0, p1.getWidth(), p1.getHeight());
                            p2.setText("Computer: " + comp.getScore());
                            p1Turn = !p1Turn;
                            computerTurn();
                        }

//

                        changeColour();
                        if (isThereAWinner == 1) {
                            //Player 1 Wins
                            wordLabel.setText("Player 1 Wins");
                            timerCounter = 5;
                            if(timerCounter == 0) {
                                dispose();
                            }
                        }
// conflict
                        timerCounter = 15;
                    }
                    else {
                        wordLabel.setText("Not a word. Try again. ");
                    }
                }
            case "Pause" -> {
                isTimerRunning = false;
                wordPrompt.remove(word);
                wordPrompt.remove(ok);
                //wordPrompt.hide();
                wordPrompt.paintImmediately(0, 0, wordPrompt.getWidth(), wordPrompt.getHeight());
                wordPrompt.revalidate();
                right.setVisible(false);
                pass.setVisible(false);
                shuffle.setVisible(false);
                repaint();
                revalidate();
            }
            case "Resume" -> {
                isTimerRunning = true;
                wordPrompt.add(word);
                wordPrompt.add(ok);
                wordPrompt.paintImmediately(0, 0, wordPrompt.getWidth(), wordPrompt.getHeight());
                wordPrompt.revalidate();
                pass.setVisible(false);
                shuffle.setVisible(false);
                right.setVisible(true);
                repaint();
                revalidate();
            }
            case "Restart" -> {
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
    public void changeColour() {
        if(p1Turn) {
            p1.setBackground(Color.BLUE);
            p2.setBackground(Color.WHITE);
            p1.setForeground(Color.WHITE);
            p2.setForeground(Color.BLACK);
        }
        else {
            p1.setBackground(Color.WHITE);
            p2.setBackground(Color.BLUE);
            p1.setForeground(Color.BLACK);
            p2.setForeground(Color.WHITE);
        }
        p1.setOpaque(true);
        p2.setOpaque(true);
        scores.paintImmediately(0,0,scores.getWidth(), scores.getHeight());
        scoreBoard.paintImmediately(0,0,scoreBoard.getWidth(), scoreBoard.getHeight());
        left.paintImmediately(0,0,left.getWidth(), left.getHeight());
    }
    public void computerTurn() {
        if (p1Turn == false && isTwoPlayers == false) {
            String compWord = "";
            comp.getWords();
            if (difficultyNumber == 1) {
                compWord = comp.getString_easy();
                targetScore.setText("Last Word: " + compWord);
                if(p1Turn == false){
                    comp.addScore(compWord);
                }
                p2.setText("Computer: " + comp.getScore());
            }
            else if (difficultyNumber == 2) {
                compWord = comp.getWord_Hard();
                targetScore.setText("Last Word: " + compWord);
                if(p1Turn == false){
                comp.addScore(compWord);
                }
                p2.setText("Computer: " + comp.getScore());
            }
            if (isThereAWinner == 2) {
                if (isTwoPlayers) wordLabel.setText("Player 2 wins. ");
                else wordLabel.setText("Computer wins. ");
                word.setText("");
                wordLabel.setText("Your Word: " + word.getText());
                p1.paintImmediately(0, 0, p1.getWidth(), p1.getHeight());
                timerCounter = 5;
                if (timerCounter == 0) {
                    dispose();
                }
// conflict
            }
            }
            countdown.paintImmediately(0, 0, countdown.getWidth(), countdown.getHeight());
            countdown.revalidate();
            revalidate();
            repaint();
                /*
                if(!p1Turn&&!isTwoPlayers) {
                    right.setVisible(false);
                    left.setVisible(false);
                    upper.setVisible(false);
                }
                else if(p1Turn&&!isTwoPlayers){
                        right.setVisible(true);
                        left.setVisible(true);
                        upper.setVisible(true);
                    }
                }
                */
    }
}
