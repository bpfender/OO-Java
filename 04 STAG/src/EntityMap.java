import java.util.ArrayList;
import java.util.HashMap;

/**
 * EntityMap
 */
public class EntityMap<T extends Entity> {
    HashMap<String, T> map = new HashMap<>();

    public T getEntity(String name) {
        return map.get(name);
    }

    public void addEntity(String name, T entity) {
        map.put(name, entity);
    }

    public void removeEntity(String name) {
        map.remove(name);
    }

    public ArrayList<String> listEntities() {
        return new ArrayList<String>(map.keySet());
    }

    public boolean contains(String name) {
        return map.containsKey(name);
    }

}