import java.util.ArrayList;

public class InventoryStrategy implements CommandStrategy {

    Player player;

    public InventoryStategy(Player player) {
        this.player = player;
    }

    @Override
    public String process() {
        String inventoryString = new String("You have in your inventory:\n");
        ArrayList<String> items = player.getInventoryList();

        if (items.size() == 0) {
            return "You have nothing in your inventory\n";
        }

        for (String i : items) {
            inventoryString.concat(i + player.getItem(i).getDescription() + "\n");
        }
        return inventoryString;
    }
}
