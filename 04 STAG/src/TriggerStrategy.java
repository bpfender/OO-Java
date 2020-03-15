/**
 * TriggerStrategy
 */
public class TriggerStrategy implements CommandStrategy {
    Player player;
    Location location;
    Game game;
    String[] command;

    public TriggerStrategy(Player player, Game game, String[] command) {
        this.player = player;
        location = player.getLocation();
        this.game = game;
        this.command = command;
    }

    @Override
    public String process() {
        Action action = getAction(command, game);
        String description = new String();

        if (action == null) {
            return "This is an invalid action\n";
        }

        for (String id : action.getSubjects()) {
            if (!checkSubjectExists(id)) {
                // FIXME, doesn't tell all
                return "Can't perform action without" + id;
            }
        }
        description = description.concat(action.getNarration());

        for (String id : action.getConsumed()) {
            consumeEntity(id);
        }
        for (String id : action.getProduced()) {
            produceEntity(id);
        }

        return description + "\n";
    }

    private boolean checkSubjectExists(String id) {
        return player.getInventoryMap().containsEntity(id) || location.getArtefactMap().containsEntity(id)
                || location.getFurnitureMap().containsEntity(id) || location.getCharacterMap().containsEntity(id);

    }

    // TODO string returns
    private void consumeEntity(String id) {
        if (id.toLowerCase().equals("health")) {
            // TODO health handling
        } else {
            if (player.getInventoryMap().removeEntity(id) != null) {
            } else if (location.getArtefactMap().removeEntity(id) != null) {
            } else if (location.getFurnitureMap().removeEntity(id) != null) {
            } else if (location.getCharacterMap().removeEntity(id) != null) {
            } else if (location.getPathMap().removeEntity(id) != null) {
            }
            // TODO consume paths?
        }
    }

    // TODO string return
    private void produceEntity(String id) {
        Location unplaced = game.getLocationMap().getEntity("unplaced");
        System.out.println(id);

        if (id.toLowerCase().equals("health")) {
            // TODO health handling
        } else {
            if (unplaced.getArtefactMap().containsEntity(id)) {
                player.getInventoryMap().addEntity(unplaced.getArtefactMap().removeEntity(id));
                System.out.println("Artefact produced" + id);
            } else if (game.getLocationMap().containsEntity(id)) {
                location.addEntity(game.getLocationMap().getEntity(id));
                System.out.println("Path produced" + id);
            } else if (unplaced.getCharacterMap().containsEntity(id)) {
                location.addEntity(unplaced.getCharacterMap().removeEntity(id));
                System.out.println("Character produced" + id);
            } else if (unplaced.getFurnitureMap().containsEntity(id)) {
                location.addEntity(unplaced.getFurnitureMap().removeEntity(id));
                System.out.println("Furniture produced" + id);
            }
        }
    }

    private Action getAction(String[] command, Game game) {
        for (int i = 1; i < command.length; i++) {
            System.out.println(command[i]);
            Action action = game.getAction(command[i].trim());
            if (action != null) {
                return action;
            }
        }
        return null;
    }

}