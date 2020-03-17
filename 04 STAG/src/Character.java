/**
 * Character
 */
public class Character extends Entity {
    private Location location;

    public Character(String id, String description, Location location) {
        super(id, description);
        this.location = location;
    }

    public void setLocation(Location newLocation) {
        this.location = newLocation;
    }

    public Location getLocation() {
        return location;
    }

}