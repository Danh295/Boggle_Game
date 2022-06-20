package Boggle;

/** '''''''''' DONE ''''''''''
 * Subclass of Player superclass: Human player
 */
public class Human extends Player {

    /**
     * Constructor method
     * @param name player's name
     * @param score player's score
     */
    public Human(String name, int score) {
        super(name, score);
    }

    /**
     * Get ID of the human player
     * @return name of the player
     */
    public String getID() { return name; }
}
