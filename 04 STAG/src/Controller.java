/**
 * CommandHandler
 */
public class Controller {
    private Game game;
    private Player player;

    public Controller(Game game) {
        this.game = game;
    }

    public String processLine(String line) {

        String input = line.toLowerCase().trim(); // Trim any leading or trailing whitespace and lowercase input

        String splitLine[] = input.split("(\\s*[:]\\s*)"); // Split on first colon and any surroumding whitespace

        if (splitLine.length == 1) {
            return "No action specified";
        }

        final String playerId = splitLine[0];
        final String[] commandList = splitLine[1].split("\\s+"); // Split command string on (any) whitespace

        if ((player = game.getPlayerMap().getEntity(playerId)) == null) {
            player = new Player(playerId, "An intrepid adventurer", game.getStartLocation());
            game.addEntity(player);
        }

        return processCommandString(game, commandList);
    }

    private String processCommandString(Game game, String[] commandList) {
        CommandStrategy strategy;

        if (commandList[0].equals("inventory") || commandList[0].equals("inv")) {
            strategy = new InventoryStrategy(player);
        } else if (commandList[0].equals("get")) {
            strategy = new GetStrategy(player, commandList);
        } else if (commandList[0].equals("drop")) {
            strategy = new DropStrategy(player, commandList);
        } else if (commandList[0].equals("goto")) {
            strategy = new GotoStrategy(player, commandList);
        } else if (commandList[0].equals("look")) {
            strategy = new LookStrategy(player);
        } else if (commandList[0].equals("health")) {
            strategy = new HealthStrategy(player);
        } else {
            strategy = new TriggerStrategy(player, game, commandList);
        }

        return strategy.process();
    }

}