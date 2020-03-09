/**
 * CommandHandler
 */
public class CommandHandler {
    String name;
    String command;
    Player player;

    public void processInput(World world, String line) {

        String split[] = line.toLowerCase().split(":");
        name = split[0];
        command = split[1];

        // FIXME what if the player doesn't exist?
        player = world.getPlayer(name);

        processCommandString(world, command);

    }

    // FIXME horribly bosic way of parsing commands. Some more sophistication please
    private void processCommandString(World world, String command) {
        if (command.contains("inventory") || command.contains("inv")) {
            player.listInventory();
        } else if (command.contains("get")) {

        } else if (command.contains("drop")) {

        } else if (command.contains("goto")) {

        } else if (command.contains("look")) {
            player.getLocation().describeLocation();
        } else if (command.contains("health")) {
            System.out.printf("Health: %s\n", player.getHealth());
        }
    }

    private String getSubject(String command) {
        String split[] = command.split(" ");
        for (String word : split) {

        }
        return "SAMPLE";
    }

    // QUESTION command semantics?
}