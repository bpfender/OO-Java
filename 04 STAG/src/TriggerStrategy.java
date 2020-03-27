public class TriggerStrategy implements CommandStrategy {
    private final Player player;
    private final Location location;
    private final Game game;
    private final String[] commandList;

    public TriggerStrategy(Player player, Game game, String[] commandList) {
        this.player = player;
        location = player.getLocation();
        this.game = game;
        this.commandList = commandList;
    }

    @Override
    public String process() {
        Action action = getAction();
        if (action == null) {
            return "This is an invalid action\n";
        }

        // Check nevessary subjects exist
        for (String id : action.getSubjects()) {
            if (!checkEntityExists(id, location)) {
                return "Can't perform action without " + id + "\n";
            }
        }

        // Check that items can be produced
        for (String id : action.getProduced()) {
            if (!(checkEntityExists(id, game.getLocationMap().getEntity("unplaced")) || id.equals("health")
                    || game.getLocationMap().containsEntity(id))) {
                return "Nothing happened\n";
            }
        }

        // Consume and produce items
        for (String id : action.getConsumed()) {
            consumeEntity(id);
        }
        for (String id : action.getProduced()) {
            produceEntity(id);
        }

        // Would this work bettery in some sort of observer pattern?
        if (player.getHealth() == 0) {
            resetPlayer();
            return action.getNarration() + "\n\n" + "You died and dropped any items in your inventory!" + "\n";
        }

        return action.getNarration() + "\n";
    }

    private boolean checkEntityExists(String id, Location location) {
        return player.getInventoryMap().containsEntity(id) || location.getArtefactMap().containsEntity(id)
                || location.getFurnitureMap().containsEntity(id) || location.getCharacterMap().containsEntity(id)
                || location.getPathMap().containsEntity(id);

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

    private void produceEntity(String id) {
        Location unplaced = game.getLocationMap().getEntity("unplaced");

        if (id.toLowerCase().equals("health")) {
            player.setHealth(player.getHealth() + 1);
        } else {
            if (unplaced.getArtefactMap().containsEntity(id)) {
                player.addEntity(unplaced.getArtefactMap().removeEntity(id));
            } else if (game.getLocationMap().containsEntity(id)) {
                location.addEntity(game.getLocationMap().getEntity(id));
            } else if (unplaced.getCharacterMap().containsEntity(id)) {
                location.addEntity(unplaced.getCharacterMap().removeEntity(id));
            } else if (unplaced.getFurnitureMap().containsEntity(id)) {
                location.addEntity(unplaced.getFurnitureMap().removeEntity(id));
            }
        }
    }

    private void resetPlayer() {
        Location startLocation = game.getStartLocation();

        startLocation.addEntity(location.getPlayerMap().removeEntity(player.getName()));
        player.setLocation(startLocation);
        player.setHealth(3);

        for (String item : player.getInventoryMap().listEntities()) {
            location.addEntity(player.getInventoryMap().removeEntity(item));
        }

    }

    private Action getAction() {
        for (String command : commandList) {
            Action action = game.getAction(command);
            if (action != null) {
                return action;
            }
        }
        return null;
    }

}