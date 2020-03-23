/**
 * Player
 */
public class Player extends Character {
    private EntityMap<Artefact> inventoryMap = new EntityMap<>();
    private int health = 3;
    private Location location;

    public Player(String id, String description, Location start) {
        super(id, description);
        this.location = start;
    }

    public EntityMap<Artefact> getInventoryMap() {
        return inventoryMap;
    }

    public void setLocation(Location newLocation) {
        this.location = newLocation;
    }

    public Location getLocation() {
        return location;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

}