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
}
