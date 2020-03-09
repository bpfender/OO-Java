import java.util.HashMap;

/**
 * Player
 */
public class Player extends Character {
    int health = 3;

    HashMap<String, Artefact> inventory = new HashMap<>();
    Location currentLocation;

    public Player(String name, String description) {
        super(name, description);
    }

    public void listInventory() {
        // FIXME seems like i'm duplicating stuff with the hashset
        inventory.keySet().stream().forEach(s -> System.out.printf("%: %\n", s, inventory.get(s)));
    }

    public void dropItem(String name) {

    }

    public void addItem(Artefact artefact) {
        inventory.put(artefact.getName(), artefact);
    }

    public void setLocation(Location location) {
        this.currentLocation = location;
    }

    public Location getLocation() {
        return currentLocation;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}