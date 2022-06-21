package Boggle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Main class to the Boggle game, contains global variables for the game as well as various utility methods
 */
public class BoggleGame extends BoggleGUI{

    public static ArrayList<String> dictionary = new ArrayList<>();
    public static char[][] board = new char[5][5];
    public static char[][] letterPossibilities = {  {'A','A','A','F','R','S'}, {'A','A','E','E','E','E'}, {'A','A','F','I','R','S'}, {'A','D','E','N','N','N'}, {'A','E','E','E','E','M'},
                                                    {'A','E','E','G','M','U'}, {'A','E','G','M','N','N'}, {'A','F','I','R','S','Y'}, {'B','J','K','Q','X','Z'}, {'C','C','N','S','T','W'},
                                                    {'C','E','I','I','L','T'}, {'C','E','I','L','P','T'}, {'C','E','I','P','S','T'}, {'D','D','L','N','O','R'}, {'D','H','H','L','O','R'},
                                                    {'D','H','H','N','O','T'}, {'D','H','L','N','O','R'}, {'E','I','I','I','T','T'}, {'E','M','O','T','T','T'}, {'E','N','S','S','S','U'},
                                                    {'F','I','P','R','S','Y'}, {'G','O','R','R','V','W'}, {'H','I','P','R','R','Y'}, {'N','O','O','T','U','W'}, {'O','O','O','T','T','U'}  };

    /**
     * Generates the game board and stores generated data into the global board 2d array
     */
    public static void generateBoard() {
        boolean[] visited = new boolean[25];
        Random rand = new Random();

        /* counter variables */
        int i = 0;
        int j = 0;

        /* iterate until valid values are generated */
        while(i<5) {
            while(j<5) {
                int x = rand.nextInt(25);
                int y = rand.nextInt(6);
                if(!visited[x]) {
                    visited[x] = true;
                    board[i][j] = letterPossibilities[x][y];
                    j++;
                }
            }
            j=0;
            i++;
        }
    }

    /**
     * Check the winner of the game given the two players
     * @param player1 the first player
     * @param player2 the second player
     * @return 1 if player 1 wins
     *         2 if player 2 wins
     *         -1 if there is no winner yet
     */
    public static int isWinner(Player player1, Player player2) {
        if (player1.getScore() >= tournamentScore) {
            return 1;
        }
        else if (player2.getScore() >= tournamentScore) {
            return 2;
        }
        else {
            return -1;
        }
    }

    /**
     * Binary search dictionary for the given string
     * @param target the string to search for
     * @param lower lower bound of the search
     * @param upper upper bound of the search
     * @return true: if the string is found
     *         false: if the string is not found
     */
    public static boolean verifyWord_Dict(String target, int lower, int upper) {
        if (lower <= upper) { // if there are more elements to check, that are still in range
            target = target.toLowerCase();

            int mid = (upper - lower) / 2; // update middle index
            int charIndex = 0; // update character index used for comparisons
            boolean found = false;
            String currWord = dictionary.get(mid+lower);

            if (currWord.equals(target)) return true; // check if current word is the target

            // check if character index needs to be updated
            while (currWord.charAt(charIndex) == target.charAt(charIndex)) {
                if (charIndex == currWord.length() - 1 || charIndex == target.length() - 1) { // if out of range
                    if (currWord.length() > target.length()) { // if word contains target
                        found = true;
                    }
                    break;
                }
                charIndex += 1;
            }

            if (currWord.charAt(charIndex) > target.charAt(charIndex) || found) { // if word's character's ascii value is greater than the characters, or if word contains the target
                return verifyWord_Dict(target, lower, lower + mid - 1); // recursive call w/ updated upper bound
            }
            return verifyWord_Dict(target, lower + mid + 1, upper); // recursive call w/ updated lower bound
        }
        return false;
    }

    /**
     * Checks whether the current game board contains the given string
     *
     * @param target the target string to search for
     * @return true: if the target can be found in the board
     * false: if the target cannot be found in the board
     */
    public static boolean verifyWord_Board(String target) {
        target = target.toUpperCase();

        boolean[][] visited = new boolean[5][5];
        String word;
        char letter;

        /* linear iteration until current character matches with target's initial character */
        for (int row = 0; row < 5; row ++) {
            for (int col = 0; col < 5; col++) {

                word = "";
                letter = board[row][col];
                word += letter;

                if (letter == target.charAt(0)) {
                    System.out.println(letter);
                    return searchWord(target, word, row, col, visited);
                }
            }
        }
        return false;
    }

    /**
     * Recursively traverses the game board in search for a word
     * @param target the target string to search for
     * @param word the current word
     * @param row the row number of the current letter on the board
     * @param col the column number of the current letter on the board
     * @param visited array to mark already visited coordinates
     * @return true: if there is a path
     */
    public static boolean searchWord(String target, String word, int row, int col, boolean[][] visited) {
        visited[row][col] = true;

        if (word.equals(target)) return true; // if current word is equal to target
        if (word.length() == target.length()) return false; // if current word is same length as target

        for (int[] neighbour : getNeighbours(row, col)) {
            if (neighbour[0] != -1) { // if neighbour is valid

                if (!visited[neighbour[0]][neighbour[1]]) { // if neighbour is unvisited
                    visited[neighbour[0]][neighbour[1]] = true;
                    word += board[neighbour[0]][neighbour[1]]; // add neighbouring letter to current word

                    if (!target.contains(word)) {
                        visited[neighbour[0]][neighbour[1]] = false;
                        word = word.substring(0, word.length() - 1);
                        continue;
                    }

                    if (word.equals(target)) return true; // if current word is equal to target
                    if (word.length() == target.length()) return false; // if current word is same length as target

                    if (searchWord(target, word, neighbour[0], neighbour[1], visited)) return true;
                    else word = word.substring(0, word.length() - 1);
                }
            }
        }
        return false;
    }

    /**
     * Helper method that returns coordinates of all neighbouring cells if they're valid, returning -1 otherwise
     * @param row number of rows
     * @param col number of columns
     * @return 2d array containing a collection of coordinates
     * if there is no valid neighbour at a position surrounding the cell, -1 is placed into the x-value
     */
    public static int[][] getNeighbours(int row, int col) {
        int[][] neighbours = new int[8][2];

        if (col + 1 < 5) neighbours[0] = new int[]{row, col + 1}; // up
        else neighbours[0][0] = -1;

        if (col - 1 > -1) neighbours[1] = new int[]{row, col - 1}; // down
        else neighbours[1][0] = -1;

        if (row - 1 > -1) {
            neighbours[2] = new int[]{row - 1, col}; // left

            if (col + 1 < 5) neighbours[5] = new int[]{row - 1, col + 1}; // up & left
            else neighbours[5][0] = -1;

            if (col - 1 > -1) neighbours[7] = new int[]{row - 1, col - 1}; // down & left
            else neighbours[7][0] = -1;

        } else neighbours[2][0] = -1;

        if (row + 1 < 5) {
            neighbours[3] = new int[]{row + 1, col}; // right

            if (col + 1 < 5) neighbours[4] = new int[]{row + 1, col + 1}; // up & right
            else neighbours[4][0] = -1;

            if (col - 1 > -1) neighbours[6] = new int[]{row + 1, col - 1}; // down & right
            else neighbours[6][0] = -1;

        } else neighbours[3][0] = -1;

        return neighbours;
    }

    /**
     * Reset variable values for new games
     */
    public static void reset() {
        player1 = null;
        player2 = null;
        // If we choose to do minimum characters
        // BoggleGUI.minChar = 0;
    }
}
