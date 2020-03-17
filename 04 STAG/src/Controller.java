/**
 * CommandHandler
 */
public class Controller {
    Game game;
    Player player;

    String playerId;
    String[] commandList;

    public Controller(Game game) {
        this.game = game;
    }

    public String processLine(String line) {
        String input = line.toLowerCase().trim(); // Trim any leading or trailing whitespace and lowercase input
        String split[] = input.split("(\\s+):(\\s+)"); // Split on first colon and any surroumding whitespace

        this.playerId = split[0];
        this.commandList = split[1].split("\\s+"); // Split command string on whitespace

        if ((player = game.getPlayerMap().getEntity(playerId)) == null) {
            player = new Player(playerId, "An intrepid adventurer", game.getStartLocation());
            game.getPlayerMap().addEntity(player);
        }

        return processCommandString(game, commandList);
    }

    // TODO variable renaming
    private String processCommandString(Game game, String[] commandList) {
        CommandStrategy strategy;

        for (String s : commandList) {
            System.out.printf("%s\n", s);
        }

        if (commandList[0].equals("inventory") || commandList[0].equals("inv")) {
            strategy = new InventoryStrategy(player);
        } else if (commandList[0].equals("get")) {
            strategy = new GetStrategy(player, commandList);
        } else if (commandList[0].equals("drop")) {
            strategy = new DropStrategy(player, commandList);
        } else if (commandList[0].equals("goto")) {
            strategy = new GotoStrategy(player, commandList);
        } else if (commandList[0].equals("look")) {
            strategy = new LookStrategy(player.getLocation());
        } else if (commandList[0].equals("health")) {
            strategy = new HealthStrategy(player);
        } else {
            strategy = new TriggerStrategy(player, game, commandList);
        }

        return strategy.process();
    }

}