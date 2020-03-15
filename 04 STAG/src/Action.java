import java.util.ArrayList;

/**
 * Actions
 */
public class Action {
    private final ArrayList<String> triggers;
    private final ArrayList<String> subjects;
    private final ArrayList<String> consumed;
    private final ArrayList<String> produced;
    private final String narration;

    public Action(ArrayList<String> triggers, ArrayList<String> subjects, ArrayList<String> consumed,
            ArrayList<String> produced, String narration) {
        this.triggers = triggers;
        this.subjects = subjects;
        this.consumed = consumed;
        this.produced = produced;
        this.narration = narration;
    }

    public ArrayList<String> getTriggers() {
        return triggers;
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }

    public ArrayList<String> getConsumed() {
        return consumed;
    }

    public ArrayList<String> getProduced() {
        return produced;
    }

    public String getNarration() {
        return narration;
    }
}
