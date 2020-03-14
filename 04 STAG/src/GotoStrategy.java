public class GotoStrategy implements CommandStrategy {
    Player player;
    String[] command;

    public GotoStrategy(Player player, String[] command) {
        this.player = player;
        this.command = command;
    }

    @Override
    public String process() {
        System.out.println(command.length);

        if (command.length == 1) {
            return "You must specify a location to go to\n";
        }
        if (command.length > 2) {
            return "You cannot go to multiple locations\n";
        }

        Location currentLocation = player.getLocation();
        Location newLocation = currentLocation.getPathMap().getEntity(command[1].strip());
        System.out.println(command[1]);

        if (newLocation == null) {
            return "Invalid path specified\n";
        } else {
            player.setLocation(newLocation);
            newLocation.addEntity(player);
            currentLocation.getPlayerMap().removeEntity(player.getName());
            return "You follow the path to the " + command[1];
        }
    }

}