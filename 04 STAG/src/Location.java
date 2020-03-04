import java.util.*;

/**
 * Locations
 */
public class Location extends Entity {
    public Location(String name, String description) {
        super(name, description);
    }

    Map<String, Location> paths;
    Map<String, Character> characters;
    Map<String, Artefact> artefacts;
    Map<String, Furniture> furniture;
    Map<String, Player> players;

}