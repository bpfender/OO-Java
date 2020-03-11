import org.json.simple.*;

/**
 * Actions
 */

// FIXME maybe make these final
// FIXME don't like that this is structured as a JSONArray at the moment
public class Action {
    JSONArray triggers = new JSONArray();
    JSONArray subjects = new JSONArray();
    JSONArray consumed = new JSONArray();
    JSONArray produced = new JSONArray();
    String narration;

    public Action(JSONArray triggers, JSONArray subjects, JSONArray consumed, JSONArray produced, String narration) {
        this.triggers = triggers;
        this.subjects = subjects;
        this.consumed = consumed;
        this.produced = produced;
        this.narration = narration;
    }

    public JSONArray getTriggers() {
        return triggers;
    }

    public JSONArray getSubject() {
        return subjects;
    }

    public JSONArray getConsumed() {
        return consumed;
    }

    public JSONArray getProduced() {
        return produced;
    }

    public String getNarration() {
        return narration;
    }

}
