/**
 * Locations
 */
public class Location extends Entity {
    private EntityMap<Location> pathMap = new EntityMap<>();
    private EntityMap<Character> characterMap = new EntityMap<>();
    private EntityMap<Artefact> artefactMap = new EntityMap<>();
    private EntityMap<Furniture> furnitureMap = new EntityMap<>();
    private EntityMap<Player> playerMap = new EntityMap<>();

    public Location(String id, String description) {
        super(id, description);
    }

    public void addEntity(Location entity) {
        pathMap.addEntity(entity);
    }

    public void addEntity(Character entity) {
        characterMap.addEntity(entity);
    }

    public void addEntity(Artefact entity) {
        artefactMap.addEntity(entity);
    }

    public void addEntity(Furniture entity) {
        furnitureMap.addEntity(entity);
    }

    public void addEntity(Player entity) {
        playerMap.addEntity(entity);
    }

    public EntityMap<Location> getPathMap() {
        return pathMap;
    }

    public EntityMap<Character> getCharacterMap() {
        return characterMap;
    }

    public EntityMap<Artefact> getArtefactMap() {
        return artefactMap;
    }

    public EntityMap<Furniture> getFurnitureMap() {
        return furnitureMap;
    }

    public EntityMap<Player> getPlayerMap() {
        return playerMap;
    }

}