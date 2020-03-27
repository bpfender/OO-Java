public class GotoStrategy implements CommandStrategy {
    private final Player player;
    private final String[] commandList;

    public GotoStrategy(Player player, String[] commandList) {
        this.player = player;
        this.commandList = commandList;
    }

    @Override
    public String process() {
        if (commandList.length == 1) {
            return "Where do you want to go?\n";
        }
        if (commandList.length > 2) {
            return "You can only go to one place at a time\n";
        }

        String locationName = commandList[1];
        Location currentLocation = player.getLocation();
        Location newLocation = currentLocation.getPathMap().getEntity(locationName);

        if (newLocation == null) {
            return "There's no path leading to " + locationName + "\n";
        } else {
            // I guess this might make sense to implement as an observer
            player.setLocation(newLocation);
            newLocation.addEntity(player);
            currentLocation.getPlayerMap().removeEntity(player.getName());

            return "You follow the path to the " + locationName + "...\n\n" + new LookStrategy(player).process();
        }
    }

}