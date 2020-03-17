/**
 * HealthStrategy
 */
public class HealthStrategy implements CommandStrategy {
    final Player player;

    public HealthStrategy(Player player) {
        this.player = player;
    }

    @Override
    public String process() {
        return new String("You have " + player.getHealth() + " HP left.\n");
    }

}