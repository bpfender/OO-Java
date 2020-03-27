import java.util.ArrayList;
import java.util.HashMap;

public class EntityMap<T extends Entity> {
    private HashMap<String, T> map = new HashMap<>();
    private int size = 0;

    public void addEntity(T entity) {
        size++;
        map.put(entity.getName(), entity);
    }

    public T getEntity(String id) {
        return map.get(id);
    }

    public T removeEntity(String id) {
        size--;
        return map.remove(id);

    }

    public ArrayList<String> listEntities() {
        return new ArrayList<String>(map.keySet());
    }

    public boolean containsEntity(String id) {
        return map.containsKey(id);
    }

    public int getSize() {
        return size;
    }
}