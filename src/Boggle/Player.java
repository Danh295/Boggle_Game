package Boggle;

/**
 * Abstract parent class for all game players
 */
public abstract class Player {
    String name;
    int score;

    public abstract String getID();
    public int getScore() { return score; }
    public void setName(String name) { this.name = name; }
    public void setScore(int score) { this.score = score; }
    public int addScore(String word) {
        int wordLength = word.length();
        if (wordLength == 3 || wordLength == 4) {
            this.score += 1;
        } else if (wordLength == 5) {
            this.score += 2;
        } else if (wordLength == 6) {
            this.score += 3;
        } else if (wordLength == 7) {
            this.score += 5;
        } else if (wordLength >= 9) {
            this.score += 11;
        }
        return this.score;
    }

}

