/**
 * Player
 */
public class Player extends Character {
    private EntityMap<Artefact> inventoryMap = new EntityMap<>();
    private int health = 3;

    public Player(String id, String description, Location start) {
        super(id, description, start);

    }

    public EntityMap<Artefact> getInventoryMap() {
        return inventoryMap;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }
}