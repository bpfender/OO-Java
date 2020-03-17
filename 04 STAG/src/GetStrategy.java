/**
 * GetStrategy
 */
public class GetStrategy implements CommandStrategy {
    final Player player;
    final String[] commandList;

    public GetStrategy(Player player, String[] commandList) {
        this.player = player;
        this.commandList = commandList;
    }

    @Override
    public String process() {
        Location location = player.getLocation();
        String output = new String();

        for (int i = 1; i < commandList.length; i++) {
            String itemName = commandList[i];

            if (location.getCharacterMap().containsEntity(itemName)
                    || location.getFurnitureMap().containsEntity(itemName)) {
                output += "Cannot pick up" + itemName + "\n";
            } else if (!location.getArtefactMap().containsEntity(itemName)) {
                output += itemName + "is not a valid item\n";
            } else {
                Artefact item = location.getArtefactMap().removeEntity(itemName);
                player.getInventoryMap().addEntity(item);

                output += "Picked up " + itemName + "\n";
            }
        }

        return output + "\n";
    }

}