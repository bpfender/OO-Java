import java.util.List;

/**
 * Player
 */
public class Player extends Character {
    public Player(String name, String description) {
        super(name, description);
    }

    List<Artefact> inventory;
}