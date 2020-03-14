public class DropStrategy implements CommandStrategy {
    Player player;
    String[] command;

    public DropStrategy(Player player, String[] command) {
        this.player = player;
        this.command = command;
    }

    @Override
    public String process() {
        String dropDescription = new String();
        Location location = player.getLocation();

        for (int i = 1; i < command.length; i++) {
            Artefact item = player.getInventoryMap().getEntity(command[i]);

            if (item == null) {
                dropDescription = dropDescription.concat("Do not have " + command[i] + " in inventory\n");
            } else {
                dropDescription = dropDescription.concat("Dropped " + item.getDescription() + "\n");
                player.getInventoryMap().removeEntity(command[i]);
                location.addEntity(item);
            }
        }

        return dropDescription;
    }

}