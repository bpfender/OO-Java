/**
 * Player
 */
public class Player extends Character {
    private EntityMap<Artefact> inventoryMap = new EntityMap<>();
    private Location currentLocation;
    private int health = 3;

    public Player(String id, String description, Location start) {
        super(id, description);
        this.currentLocation = start;
    }

    public EntityMap<Artefact> getInventoryMap() {
        return inventoryMap;
    }

    public void setLocation(Location newLocation) {
        this.currentLocation = newLocation;
    }

    public Location getLocation() {
        return currentLocation;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }
}