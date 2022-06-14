package Boggle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Main class to the Boggle game, will contain global variables for the whole project as well as utility methods
 */
public class BoggleGame {
    public static int targetScore;
    public static ArrayList<String> dictionary = new ArrayList<>();
    public static int maxWordLength;
    public static char[][] board = new char[5][5];
    public static char[][] letterPossibilities = {  {'A','A','A','F','R','S'},
                                                    {'A','A','E','E','E','E'},
                                                    {'A','A','F','I','R','S'},
                                                    {'A','D','E','N','N','N'},
                                                    {'A','E','E','E','E','M'},
                                                    {'A','E','E','G','M','U'},
                                                    {'A','E','G','M','N','N'},
                                                    {'A','F','I','R','S','Y'},
                                                    {'B','J','K','Q','X','Z'},
                                                    {'C','C','N','S','T','W'},
                                                    {'C','E','I','I','L','T'},
                                                    {'C','E','I','L','P','T'},
                                                    {'C','E','I','P','S','T'},
                                                    {'D','D','L','N','O','R'},
                                                    {'D','H','H','L','O','R'},
                                                    {'D','H','H','N','O','T'},
                                                    {'D','H','L','N','O','R'},
                                                    {'E','I','I','I','T','T'},
                                                    {'E','M','O','T','T','T'},
                                                    {'E','N','S','S','S','U'},
                                                    {'F','I','P','R','S','Y'},
                                                    {'G','O','R','R','V','W'},
                                                    {'H','I','P','R','R','Y'},
                                                    {'N','O','O','T','U','W'},
                                                    {'O','O','O','T','T','U'}  };

    public static void main(String[] args) throws IOException {
        File dict = new File("dictionary.txt");
        Scanner dictReader = new Scanner(dict);
        while (dictReader.hasNext()) {
            dictionary.add(dictReader.next());

        }

        BoggleGUI myFrame = new BoggleGUI();
    }

    /**
     * Generates the game board and stores generated data into the global board 2d array
     */
    public static void generateBoard() {
        boolean[] visited = new boolean[25];
        Random rand = new Random();
        int i = 0;
        int j = 0;
        while(i<5) {
            while(j<5) {
                int x = rand.nextInt(24);
                int y = rand.nextInt(5);
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

        if (player1.getScore() > targetScore) {
            return 1;
        }
        else if (player2.getScore() > targetScore) {
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
            int mid = (upper - lower) / 2; // update middle index
            boolean found = false;
            int currChar = 0; // update character index

            // check if current element = target
            if (dictionary.get(mid+lower).equals(target)) {
                return true;
            }

            // check if character index needs to be updated
            while (dictionary.get(mid).charAt(currChar) == target.charAt(currChar)) {
                if (currChar == dictionary.size() - 1 || currChar == target.length() - 1) { // if out of range
                    if (dictionary.get(mid).length() > target.length()) { // if word contains target
                        found = true;
                    }
                    break;
                }
                currChar += 1;
            }

            if (dictionary.get(mid).charAt(currChar) > target.charAt(currChar) || found) { // if word's character's ascii value is greater than the characters, or if word contains the target
                return verifyWord_Dict(target, lower, mid - 1); // recursive call w/ updated upper bound
            }
            return verifyWord_Dict(target, mid + 1, upper); // recursive call w/ updated lower bound
        }
        return false;
    }

    public static boolean verifyWord_Board(char[][] board, String target) {
        boolean[][] visited = new boolean[5][5];
        for (int row = 0; row < 5; row ++) {
            for (int col = 0; col < 5; col++) {
                if (board[row][col] == target.charAt(0)) {
                    return searchWord(target, row, col, 1, visited);
                }
            }
        }
        return false;
    }

    /**
     * Recursively traverses the game board in search for a word
     * @param target the target string to search for
     * @param row the row number of the current letter on the board
     * @param col the column number of the current letter on the board
     * @param index the current index position of the character in the word
     * @param visited array to mark already visited coordinates
     * @return true if there is a path
     */
    public static boolean searchWord(String target, int row, int col, int index, boolean[][] visited) {
        if (index == target.length()) {
            return false;
        }

        int[][] neighbours = getNeighbours(row, col);
        for (int[] neighbour : neighbours) {
            if (neighbour[0] != -1 && !visited[neighbour[0]][neighbour[1]]) {
                visited[neighbour[0]][neighbour[1]] = true;
                if (index == target.length()-1 && target.charAt(index) == board[neighbour[0]][neighbour[1]])
                    return true;

                return searchWord(target, row, col, index + 1, visited);
            }
        }
        return false;
    }

    /**
     * Helper method that returns coordinates of all neighbouring cells if they're valid, returning -1 otherwise
     * @param row number of rows
     * @param col number of columns
     * @return 2d array containing a collection of coordinates
     */
    public static int[][] getNeighbours(int row, int col) {
        int[][] neighbours = new int[8][2];

        if (col+1 < 5) neighbours[0] = new int[] {row, col+1}; // up
        else neighbours[0][0] = -1;

        if (col-1 > -1) neighbours[1] = new int[] {row, col-1}; // down
        else neighbours[1][0] = -1;

        if (row-1 > -1) {
            neighbours[2] = new int[] {row-1, col}; // left

            if (col+1 < 5) neighbours[5] = new int[] {row-1, col+1}; // up & left
            else neighbours[5][0] = -1;

            if (col-1 > -1) neighbours[7] = new int[] {row-1, col-1}; // down & left
            else neighbours[7][0] = -1;

        } else neighbours[2][0] = -1;

        if (row+1 < 5) {
            neighbours[3] = new int[] {row+1, col}; // right

            if (col+1 < 5) neighbours[4] = new int[] {row+1, col+1}; // up & right
            else neighbours[4][0] = -1;

            if (col-1 > -1) neighbours[6] = new int[] {row+1, col-1}; // down & right
            else neighbours[6][0] = -1;

        } else neighbours[3][0] = -1;

        return neighbours;
    }


    /**
     * Reset variable values for new games
     */
    public static void reset() {
        /*
        delete player instances
        targetScore
        minimum character amount

        */
    }
}
