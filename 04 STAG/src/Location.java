import java.util.*;

/**
 * Locations
 */
public class Location extends Entity {
    public Location(String name, String description) {
        super(name, description);
    }

    Map<String, Location> paths = new HashMap<>();
    Map<String, Character> characters = new HashMap<>();
    Map<String, Artefact> artefacts = new HashMap<>();
    Map<String, Furniture> furniture = new HashMap<>();
    Map<String, Player> players = new HashMap<>();

    // FIXME this is super horrible
    public <T extends Entity> void addEntity(T entity) {

        if (entity instanceof Location) {
            paths.put(entity.getName(), (Location) entity);
        } else if (entity instanceof Character) {
            characters.put(entity.getName(), (Character) entity);
        } else if (entity instanceof Artefact) {
            artefacts.put(entity.getName(), (Artefact) entity);
        } else if (entity instanceof Furniture) {
            furniture.put(entity.getName(), (Furniture) entity);
        } else if (entity instanceof Player) {
            players.put(entity.getName(), (Player) entity);
        }
    }

    // TODO needs to be extended
    public void describeLocation() {
        // QUESTION this.getName() seems a bit unecesarry?
        System.out.printf("This is the %s\n", this.getName());
    }

    public Entity removeEntity(String name) {
        Entity entity;

        // FIXME super horrible at the moment
        if ((entity = paths.get(name)) != null) {
            paths.remove(name);
            return entity;
        } else if ((entity = artefacts.get(name)) != null) {
            artefacts.remove(name);
            return entity;
        } else if ((entity = furniture.get(name)) != null) {
            furniture.remove(name);
            return entity;
        } else {
            return null;
        }
    }

}