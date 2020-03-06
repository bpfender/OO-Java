import java.util.HashMap;

/**
 * World
 */
// TODO should this be a singleton?

public class World {
    HashMap<String, Location> locations = new HashMap<String, Location>();
    HashMap<String, Action> actions = new HashMap<String, Action>();

    public void addLocation(String name, Location location) {
        locations.put(name, location);
    }

    public Location getLocation(String name) {
        return locations.get(name);
    }

    public void addAction(String name, Action action) {
        actions.put(name, action);
    }

    public Action getAction(String name) {
        return actions.get(name);
    }
}