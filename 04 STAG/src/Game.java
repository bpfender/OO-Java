import java.util.HashMap;

/**
 * World
 */
// TODO should this be a singleton?

public class Game {
    private EntityMap<Location> locationMap = new EntityMap<>();
    private EntityMap<Player> playerMap = new EntityMap<>();;
    private EntityMap<Character> characterMap = new EntityMap<>();

    private HashMap<String, Action> actions = new HashMap<String, Action>();
    private Location startLocation;

    public void addEntity(Location entity) {
        locationMap.addEntity(entity);
    }

    public void addEntity(Player entity) {
        playerMap.addEntity(entity);
    }

    public void addEntity(Character entity) {
        characterMap.addEntity(entity);
    }

    public EntityMap<Character> getCharacterMap() {
        return characterMap;
    }

    public EntityMap<Location> getLocationMap() {
        return locationMap;
    }

    public EntityMap<Player> getPlayerMap() {
        return playerMap;
    }

    public void addAction(String name, Action action) {
        actions.put(name, action);
    }

    public Action getAction(String name) {
        return actions.get(name);
    }

    public void setStartLocation(Location start) {
        this.startLocation = start;
    }

    public Location getStartLocation() {
        return startLocation;
    }
}