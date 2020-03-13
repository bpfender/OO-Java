/**
 * GetStrategy
 */
public class GetStrategy implements CommandStrategy {
    Player player;
    String[] command;

    public GetStrategy(Player player, String[] command) {
        this.player = player;
        this.command = command;
    }

    @Override
    public String process() {
        Location location = player.getLocation();
        String getString = new String();

        for (int i = 1; i < command.length; i++) {
            Artefact item = location.getArtefact(command[i]);
            if (item == null) {
                // TODO error handling for furniture
                getString.concat(command[i] + "is not a valid item\n");
            } else {
                getString.concat("Picked up " + item.getDescription());
                location.removeEntity(command[i]);
                player.addItem(item);
            }
        }
        return getString;

    }

}