package Boggle;

import java.util.ArrayList;
import java.util.Random;

import static Boggle.BoggleGame.verifyWord_Board;

// todo: comment this class

/**
 * Subclass of Player superclass: Computer player
 */
public class Computer extends Player {
    private final int difficulty;
    private final ArrayList<String> usedWords = new ArrayList<>();
    private final ArrayList<String> possibleWords = new ArrayList<>();

    public Computer (String name, int score, int difficulty) {
        super();
        this.difficulty = difficulty;
    }

    public String getID() {return ("computer" + difficulty); }

    /* todo: optimize this algorithm (currently linear and too slow)
     *  search each node on board, and compare each added letter to possible words in the dictionary
     */
    public void getWords (char[] board, String[] dictionary) {

        for (String word : dictionary) {
            if (verifyWord_Board(board, word)) { // todo: implement verifyWord_Board method
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
