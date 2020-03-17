/**
 * TriggerStrategy
 */
public class TriggerStrategy implements CommandStrategy {
    final Player player;
    final Location location;
    final Game game;
    final String[] command;

    public TriggerStrategy(Player player, Game game, String[] command) {
        this.player = player;
        location = player.getLocation();
        this.game = game;
        this.command = command;
    }

    @Override
    public String process() {
        Action action = getAction(command, game);
        if (action == null) {
            return "This is an invalid action\n";
        }

        for (String id : action.getSubjects()) {
            if (!checkSubjectExists(id)) {
                return "Can't perform action without" + id + "\n";
            }
        }

        for (String id : action.getConsumed()) {
            consumeEntity(id);
        }
        for (String id : action.getProduced()) {
            produceEntity(id);
        }

        // TODO this is very unneat at the moment

        if (player.getHealth() == 0) {
            resetPlayer();
            return action.getNarration() + "\n" + "You died!" + "\n";
        }

        return action.getNarration() + "\n";
    }

    private boolean checkSubjectExists(String id) {
        return player.getInventoryMap().containsEntity(id) || location.getArtefactMap().containsEntity(id)
                || location.getFurnitureMap().containsEntity(id) || location.getCharacterMap().containsEntity(id);

    }

    private void consumeEntity(String id) {
        if (id.toLowerCase().equals("health")) {
            player.setHealth(player.getHealth() - 1);
        } else {
            if (player.getInventoryMap().removeEntity(id) != null) {
            } else if (location.getArtefactMap().removeEntity(id) != null) {
            } else if (location.getFurnitureMap().removeEntity(id) != null) {
            } else if (location.getCharacterMap().removeEntity(id) != null) {
            } else if (location.getPathMap().removeEntity(id) != null) {
            }
        }
    }

    private void resetPlayer() {
        game.getStartLocation().addEntity(location.getPlayerMap().removeEntity(player.getName()));
        player.setLocation(game.getStartLocation());
        player.setHealth(3);

        for (String item : player.getInventoryMap().listEntities()) {
            location.addEntity(player.getInventoryMap().removeEntity(item));
        }

    }

    private void produceEntity(String id) {
        Location unplaced = game.getLocationMap().getEntity("unplaced");

        if (id.toLowerCase().equals("health")) {
            player.setHealth(player.getHealth() + 1);
        } else {
            if (unplaced.getArtefactMap().containsEntity(id)) {
                player.getInventoryMap().addEntity(unplaced.getArtefactMap().removeEntity(id));
            } else if (game.getLocationMap().containsEntity(id)) {
                location.addEntity(game.getLocationMap().getEntity(id));
            } else if (unplaced.getCharacterMap().containsEntity(id)) {
                location.addEntity(unplaced.getCharacterMap().removeEntity(id));
            } else if (unplaced.getFurnitureMap().containsEntity(id)) {
                location.addEntity(unplaced.getFurnitureMap().removeEntity(id));
            }
        }
    }

    private Action getAction(String[] command, Game game) {
        for (int i = 1; i < command.length; i++) {
            Action action = game.getAction(command[i].trim());
            if (action != null) {
                return action;
            }
        }
        return null;
    }

}