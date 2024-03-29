/**
 * Entity
 */
public abstract class Entity {
    private final String id;
    private final String description;

    public Entity(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getName() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
