public class DropStrategy implements CommandStrategy {
    final Player player;
    final String[] commandList;

    public DropStrategy(Player player, String[] commandList) {
        this.player = player;
        this.commandList = commandList;
    }

    @Override
    public String process() {
        Location location = player.getLocation();
        String output = new String();

        for (int i = 1; i < commandList.length; i++) {
            String itemName = commandList[i];

            if (!player.getInventoryMap().containsEntity(itemName)) {
                output += "Do not have " + itemName + " in inventory\n";
            } else {
                Artefact item = player.getInventoryMap().removeEntity(itemName);
                location.addEntity(item);

                output = output.concat("Dropped " + itemName + "\n");
            }
        }

        return output + "\n";
    }

}