import java.util.ArrayList;

/**
 * TriggerStrategy
 */
public class TriggerStrategy implements CommandStrategy {
    Player player;
    World world;
    String[] command;

    public TriggerStrategy(Player player, World world, String[] command) {
        this.player = player;
        this.world = world;
        this.command = command;
    }

    @Override
    public String process() {
        Action action = getAction(command, world);

        if (action == null) {
            return "This is an invalid action\n";
        } else {

        }

    }

    private String checkSubjectsExist(ArrayList<String> subjects, Player player) {
        Location location = player.getLocation();
        String subjectString = new String();

        for (String s : subjects) {
            if (player.getItem(s) == null && location.containsEntity(s)) {

            }
        }
    }

    private String checkConsumedExist(ArrayList<String> consumed) {
        for()
    }

    private Action getAction(String[] command, World world) {
        for (int i = 1; i < command.length; i++) {
            Action action = world.getAction(command[i]);
            if (action != null) {
                return action;
            }
        }
        return null;
    }

}