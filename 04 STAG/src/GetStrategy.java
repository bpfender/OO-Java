/**
 * GetStrategy
 */
public class GetStrategy implements CommandStrategy {
    private final Player player;
    private final String[] commandList;

    public GetStrategy(Player player, String[] commandList) {
        this.player = player;
        this.commandList = commandList;
    }

    @Override
    public String process() {
        Location location = player.getLocation();
        String output = new String();

        if (commandList.length == 1) {
            return "What do you want to pick up?\n";
        }

        for (int i = 1; i < commandList.length; i++) {
            String itemName = commandList[i];

            if (location.getCharacterMap().containsEntity(itemName)
                    || location.getFurnitureMap().containsEntity(itemName)) {
                output += "Cannot pick up " + itemName + "\n";
            } else if (!location.getArtefactMap().containsEntity(itemName)) {
                output += itemName + " is not a valid item\n";
            } else {
                Artefact item = location.getArtefactMap().removeEntity(itemName);
                player.addEntity(item);

                output += "Picked up " + item.getDescription().toLowerCase() + "\n";
            }
        }

        return output;
    }

}