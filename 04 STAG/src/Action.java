import java.util.List;

/**
 * Actions
 */
public abstract class Action {
    List<String> triggers;
    List<String> subjects;
    List<String> consumed;
    List<String> produced;
    List<String> narration;
}