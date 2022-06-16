package Boggle;

import java.util.ArrayList;
import java.util.Random;

import static Boggle.BoggleGame.verifyWord_Board;

/**
 * Subclass of Player superclass: Computer player
 */
public class Computer extends Player {

    private final int difficulty;
    private final ArrayList<String> usedWords = new ArrayList<>();
    private final ArrayList<String> possibleWords = new ArrayList<>();

    /**
     * Constructor method for computer player
     * @param name player's name (randomly generate for computer player)
     * @param score player's score
     * @param difficulty computer player's difficulty level
     */
    public Computer (String name, int score, int difficulty) {
        super();
        this.difficulty = difficulty;
    }

    public String getID() { return ("computer" + difficulty + ": " + name); }

    /**
     * Returns all valid words currently on the board
     * @param dictionary string array of the words in the dictionary
     */
    public void getWords (String[] dictionary) {
        /* todo: optimize this algorithm (currently linear and too slow)
         *  search each node on board, and compare each added letter to possible words in the dictionary
         */
        for (String word : dictionary) {
            if (verifyWord_Board(word)) { // todo: implement verifyWord_Board method
                possibleWords.add(word);
            }
        }
    }

    public String getString_easy() {
        String minWord = possibleWords.get(0);

        for (String word : possibleWords) {
            if (!usedWords.contains(word) && word.length() < minWord.length()) {
                minWord = word;
            }
        }

        usedWords.add(minWord);
        return minWord;
    }

    public String getWord_Medium() {
        Random random = new Random();

        int index = random.nextInt(0, possibleWords.size()-1);
        while (usedWords.contains(possibleWords.get(index))) {
            index = random.nextInt(0, possibleWords.size()-1);
        }

        usedWords.add(possibleWords.get(index));
        return possibleWords.get(index);
    }
}
