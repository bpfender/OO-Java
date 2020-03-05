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

}