package Boggle;

import java.util.ArrayList;

/** '''''''''' DONE ''''''''''
 * Abstract parent class for all game players
 */
public abstract class Player {
    public String name;
    public int score;


    public final ArrayList<String> usedWords = new ArrayList<>();
    public final ArrayList<String> possibleWords = new ArrayList<>();

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /* accessor methods */
    public abstract String getID();
    public int getScore() { return score; }

    /* modifier methods */
    public void setName(String name) { this.name = name; }
    public void setScore(int score) { this.score = score; }

    /**
     * Adds to the player's score depending on the length of the given word
     * @param word word that the player successfully guessed
     */
    public void addScore(String word) {
        int wordLength = word.length();
        if (wordLength == 3 || wordLength == 4) score += 1;
        else if (wordLength == 5) score += 2;
        else if (wordLength == 6) score += 3;
        else if (wordLength == 7) score += 5;
        else if (wordLength >= 9) score += 11;
    }
}

