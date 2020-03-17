import java.util.ArrayList;

public class InventoryStrategy implements CommandStrategy {

    final Player player;

    public InventoryStrategy(Player player) {
        this.player = player;
    }

    @Override
    public String process() {
        ArrayList<String> items = player.getInventoryMap().listEntities();

        if (items.size() == 0) {
            return "You have nothing in your inventory\n";
        }

        String output = new String("You have some items in your inventory:\n");

        for (String i : items) {
            output += i.toUpperCase() + ": " + player.getInventoryMap().getEntity(i).getDescription() + "\n";
        }

        return output + "\n";
    }
}
