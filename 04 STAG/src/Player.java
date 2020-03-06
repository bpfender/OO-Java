import java.util.List;

/**
 * Player
 */
public class Player extends Character {
    int health = 3;

    List<Artefact> inventory;
    Location currentLocations;

    public Player(String name, String description) {
        super(name, description);
    }

}