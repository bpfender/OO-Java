public class GotoStrategy implements CommandStrategy {
    final Player player;
    final String[] commandList;

    public GotoStrategy(Player player, String[] commandList) {
        this.player = player;
        this.commandList = commandList;
    }

    @Override
    public String process() {
        if (commandList.length == 1) {
            return "You must specify a location to go to\n\n";
        }
        if (commandList.length > 2) {
            return "You cannot go to multiple locations\n\n";
        }

        String locationName = commandList[1];
        Location currentLocation = player.getLocation();
        Location newLocation = currentLocation.getPathMap().getEntity(locationName);

        if (newLocation == null) {
            return "There's no path leading to" + locationName + "\n\n";
        } else {
            // FIXME todo as observer?
            player.setLocation(newLocation);
            newLocation.addEntity(player);
            currentLocation.getPlayerMap().removeEntity(player.getName());

            return "You follow the path to the " + locationName + "\n\n" + new LookStrategy(player).process();
        }
    }

}