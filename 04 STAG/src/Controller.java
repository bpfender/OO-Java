/**
 * CommandHandler
 */
public class Controller {
    String id;
    String command;
    Player player;

    public void processInput(Game game, String line) {

        String split[] = line.toLowerCase().split(":");
        id = split[0];
        command = split[1];

        // FIXME what if the player doesn't exist?

        if ((player = game.getPlayerMap().getEntity(id)) == null) {
            player = new Player(id, "An adventurer", game.getStartLocation());
            game.getPlayerMap().addEntity(player);
        }

        processCommandString(game, command);
    }

    // TODO variable renaming
    private void processCommandString(Game game, String command) {
        // QUESTION how does it handle multiple whitespace?
        // TODO slightly nicer string handling please
        String split[] = command.trim().split("\\s+");

        CommandStrategy strategy;

        for (String s : split) {
            System.out.printf("%s\n", s);
        }

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
            strategy = new TriggerStrategy(player, game, split);
        }

        String string = strategy.process();
        System.out.printf("%s\n", string);
    }

}