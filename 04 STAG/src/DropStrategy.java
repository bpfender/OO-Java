public class DropStrategy implements CommandStrategy {
    private final Player player;
    private final String[] commandList;

    public DropStrategy(Player player, String[] commandList) {
        this.player = player;
        this.commandList = commandList;
    }

    @Override
    public String process() {
        Location location = player.getLocation();
        String output = new String();

        if (commandList.length == 1) {
            return "What do you want to drop?\n";
        }

        for (int i = 1; i < commandList.length; i++) {
            String itemName = commandList[i];

            if (!player.getInventoryMap().containsEntity(itemName)) {
                output += "Do not have " + itemName + " in inventory\n";
            } else {
                location.addEntity(player.getInventoryMap().removeEntity(itemName));

                output = output.concat("Dropped " + itemName + "\n");
            }
        }

        return output;
    }

}