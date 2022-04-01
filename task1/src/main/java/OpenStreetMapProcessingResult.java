import java.util.HashMap;
import java.util.Map;

public class OpenStreetMapProcessingResult {
    private final Map<String, Integer> users = new HashMap<>();
    private final Map<String, Integer> tags = new HashMap<>();

    public void incrementUserCount(String user) {
        users.compute(user, (k, v) -> (v == null) ? 1 : v + 1);
    }

    public void incrementKeyCount(String key) {
        tags.compute(key, (k, v) -> (v == null) ? 1 : v + 1);
    }

    public Map<String, Integer> getUsers() {
        return users;
    }

    public Map<String, Integer> getKeys() {
        return tags;
    }
}
