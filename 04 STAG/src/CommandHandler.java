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

    // QUESTION how does it handle multiple whitespace?

    // TODO variable renaming
    private void processCommandString(World world, String command) {
        String split[] = command.split(" ");

        CommandStrategy strategy;

        if (command.contains("inventory") || command.contains("inv")) {
            strategy = new InventoryStrategy(player);
        } else if (command.contains("get")) {
            strategy = new GetStrategy(player, split);
        } else if (command.contains("drop")) {
            strategy = new DropStrategy(player, split);
        } else if (command.contains("goto")) {
            strategy = new GotoStrategy(player, split);
        } else if (command.contains("look")) {
            strategy = new LookStrategy(player.getLocation());
        } else if (command.contains("health")) {
            strategy = new HealthStrategy(player);
        } else {
            strategy = new TriggerStrategy();
        }

        String string = strategy.process();
        System.out.printf("%s\n", string);
    }

}