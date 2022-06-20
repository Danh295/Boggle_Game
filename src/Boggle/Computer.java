package Boggle;

import java.util.ArrayList;
import java.util.Random;

import static Boggle.BoggleGame.*;

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
     */
    /**
     * Returns all valid words currently on the board
     */
    public void getWords() {
        String word;
        char letter;

        // linear iteration through letters on board
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                word = "";
                letter = board[r][c];

                word += letter; // add current letter to word string
                if (!usedWords.contains(word)) {

                    int checkContain = checkDict(word, 0, dictionary.size(), false);

                    if (checkContain == 1) { // if dictionary contains word/letters, add it to the list
                        possibleWords.add(word);
                    } else if (checkContain == -1) { // if dictionary doesn't contain the word/letters, move on to the next letter
                        continue;
                    }
                    boolean[][] visited = new boolean[5][5];
                    checkNeighbours(word, r, c, visited, new ArrayList<>()); // dfs traverse neighbours until there are no more valid words from this letter
                }
            }
        }
    }

    /** tested
     * Conditionally binary search dictionary
     * @param target target to search for
     * @param lower the lower bound of the search
     * @param upper the upper bound of the search
     * @param contains whether the dictionary contains the word (can be within one of the words)
     * @return 1: if the current word is in the dictionary
     *         0: if the word exists somewhere in the dictionary
     *         -1: if the word doesn't exist anywhere in the dictionary
     */
    public int checkDict(String target, int lower, int upper, boolean contains) {
        if (lower < upper) {
            target = target.toLowerCase();
            int mid = (upper - lower) / 2;
            int charIndex = 0; // character index to compare
            boolean inCurrWord = false;
            String currWord = dictionary.get(mid+lower);

            if (currWord.equals(target)) return 1; // check if current word equals the target

            while (currWord.charAt(charIndex) == target.charAt(charIndex)) { // iterate through character indexes to compare
                if (charIndex == currWord.length() - 1 || charIndex == target.length() - 1) { // if index is out of range
                    if (currWord.length() > target.length()) { // if the current word contains the target
                        inCurrWord = true;
                        contains = true;
                    }
                    break;
                }
                charIndex += 1; // increment character index to compare
            }

            // binary search lower half of range
            if (currWord.charAt(charIndex) > target.charAt(charIndex) || inCurrWord) {
                return checkDict(target, lower, lower + mid - 1, contains);
            }

            // binary search upper half
            return checkDict(target, lower + mid + 1, upper, contains);
        }
        if (contains) return 0;
        return -1;
    }

    /**
     * Conditionally recursively traverse game-board to search for words
     * @param word
     * @param row
     * @param col
     * @param visited
     */
    public void checkNeighbours(String word, int row, int col, boolean[][] visited, ArrayList<String> guessed) {
        visited[row][col] = true;
        for (int[] neighbour : getNeighbours(row, col)) {
            if (neighbour[0] != -1) { // neighbour is valid

                if (!visited[neighbour[0]][neighbour[1]]) { // neighbour is unvisited
                    visited[neighbour[0]][neighbour[1]] = true;

                    word += board[neighbour[0]][neighbour[1]]; // add neighbouring letter to word
                    if (!guessed.contains(word)) {
                        guessed.add(word);

                        int checkContain = checkDict(word, 0, dictionary.size(), false); // check containment of current word
                        if (checkContain == 1) { // if dictionary contains word/letters, add it to the list
                            possibleWords.add(word);
                        } else if (checkContain == -1) { // if dictionary doesn't contain the word/letters, move on to the next letter
                            word = word.substring(0, word.length() - 1);
                            continue;
                        }
                        checkNeighbours(word, neighbour[0], neighbour[1], visited, guessed); // recursive call on neighbouring cells
                    }
                    word = word.substring(0, word.length() - 1); // remove the last letter when backtracking
                }
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
