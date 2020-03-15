import java.util.ArrayList;
import java.util.HashMap;

/**
 * EntityMap
 */
public class EntityMap<T extends Entity> {
    private HashMap<String, T> map = new HashMap<>();

    public void addEntity(T entity) {
        map.put(entity.getName(), entity);
    }

    public T getEntity(String id) {
        return map.get(id);
    }

    public T removeEntity(String id) {
        return map.remove(id);
    }

    public ArrayList<String> listEntities() {
        return new ArrayList<String>(map.keySet());
    }

    // FIXME might not actually be needed

    public boolean containsEntity(String id) {
        return map.containsKey(id);
    }

}