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
        if ((player = world.getPlayer(name)) == null) {
            player = new Player(name, "An adventurer", world.getStart());
            world.addPlayer(name, player);
        }

        processCommandString(world, command);
    }

    // FIXME horribly bosic way of parsing commands. Some more sophistication please
    // TODO this assumed action verb is the first part of the string, only handles
    // one item at the moment.
    private void processCommandString(World world, String command) {
        String split[] = command.split(" ");
        Entity item;

        if (command.contains("inventory") || command.contains("inv")) {
            player.listInventory();
        } else if (command.contains("get")) {
            item = player.getLocation().removeEntity(split[1]);

        } else if (command.contains("drop")) {

        } else if (command.contains("goto")) {

        } else if (command.contains("look")) {
            player.getLocation().describeLocation();
        } else if (command.contains("health")) {
            System.out.printf("Health: %s\n", player.getHealth());
        } else if (world.getAction(split[0]) != null) {

        } else {
            System.out.println("Error in command input");
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