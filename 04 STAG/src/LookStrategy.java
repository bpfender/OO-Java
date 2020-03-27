public class LookStrategy implements CommandStrategy {
    private final Location location;
    private final Player player;

    public LookStrategy(Player player) {
        this.location = player.getLocation();
        this.player = player;
    }

    @Override
    public String process() {
        String output = new String();
        output += location.getDescription() + "...\n\n";
        output += reportFurniture();
        output += reportArtefacts();
        output += reportCharacters();
        output += reportPlayers();
        output += reportPaths();

        return output;
    }

    private String reportFurniture() {
        EntityMap<Furniture> furnitureMap = location.getFurnitureMap();
        String furnitureDescription = new String();

        if (furnitureMap.getSize() > 0) {
            furnitureDescription += "You look around and see...\n";

            for (String item : furnitureMap.listEntities()) {
                furnitureDescription += "  -" + location.getFurnitureMap().getEntity(item).getDescription() + "\n";
            }

            furnitureDescription += "\n";
        }

        return furnitureDescription;

    }

    private String reportArtefacts() {
        EntityMap<Artefact> artefactMap = location.getArtefactMap();
        String artefactDescription = new String();

        if (artefactMap.getSize() == 1) {
            artefactDescription += "There's a " + artefactMap.listEntities().get(0) + " lying on the ground.\n";
        } else if (artefactMap.getSize() > 1) {
            artefactDescription += "Several items including a ";
            for (String item : artefactMap.listEntities()) {
                artefactDescription += item + " and ";
            }
            artefactDescription = artefactDescription.replaceAll("(and )$", "");

            artefactDescription += "are strewn around.\n";
        }

        return artefactDescription;
    }

    private String reportCharacters() {
        EntityMap<Character> characterMap = location.getCharacterMap();
        String characterDescription = new String();

        if (characterMap.getSize() > 0) {
            characterDescription += "Some curious faces are peering at you. You spot...\n";
            for (String item : characterMap.listEntities()) {
                characterDescription += characterMap.getEntity(item).getDescription() + "\n";
            }
            characterDescription += "\n";
        }

        return characterDescription;
    }

    private String reportPlayers() {
        EntityMap<Player> playerMap = location.getPlayerMap();
        String playerDescription = new String();

        if (playerMap.getSize() > 1) {
            playerDescription += "You see some other intrepid adventurers, ";
            for (String name : playerMap.listEntities()) {
                if (!name.equals(player.getName())) {
                    playerDescription += name + " and ";
                }
            }
            playerDescription = playerDescription.replaceAll("(and )$", "");
            playerDescription += ", travelling the world.\n";
        }

        return playerDescription;
    }

    private String reportPaths() {
        EntityMap<Location> pathMap = location.getPathMap();
        String pathDescription = new String();

        if (pathMap.getSize() == 0) {
            pathDescription += "\nUnfortunately you can't see any paths leading away. You are stuck here...\n";
        } else if (pathMap.getSize() == 1) {
            pathDescription += "\nYou see a path going to the " + pathMap.listEntities().get(0) + ".\n";
        } else {
            pathDescription += "You see a number of paths going to the ";

            for (String item : pathMap.listEntities()) {
                pathDescription = pathDescription.concat(item + ", or a ");
            }
            pathDescription = pathDescription.replaceAll("(, or a )$", "");
            pathDescription += ", leading away.\n";
        }

        return pathDescription;
    }
}