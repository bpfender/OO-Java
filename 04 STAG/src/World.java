import java.util.HashMap;

/**
 * World
 */
// TODO should this be a singleton?

public class World {
    HashMap<String, Location> locations = new HashMap<String, Location>();

    public void addLocation(String name, Location location) {
        locations.put(name, location);
    }

    public Location getLocation(String name) {
        return locations.get(name);
    }
}