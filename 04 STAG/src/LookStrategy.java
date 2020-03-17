public class LookStrategy implements CommandStrategy {
    final Location location;
    final Player player;

    public LookStrategy(Player player) {
        this.location = player.getLocation();
        this.player = player;
    }

    @Override
    public String process() {
        String output = new String();

        EntityMap<Furniture> furnitureMap = location.getFurnitureMap();
        EntityMap<Artefact> artefactMap = location.getArtefactMap();
        EntityMap<Character> characterMap = location.getCharacterMap();
        EntityMap<Location> pathMap = location.getPathMap();
        EntityMap<Player> playerMap = location.getPlayerMap();

        output += location.getDescription() + "\n";

        if (furnitureMap.getSize() > 0) {
            output += "You look around and see...\n";

            for (String item : furnitureMap.listEntities()) {
                output += location.getFurnitureMap().getEntity(item).getDescription();
            }
        }

        if (artefactMap.getSize() == 1) {
            output += "\nThere's a " + artefactMap.listEntities().get(0) + " lying on the ground.\n";
        } else if (artefactMap.getSize() > 1) {
            output += "Several items, including a ";
            for (String item : artefactMap.listEntities()) {
                output += item + " and ";
            }
            output = output.replaceAll("(and )$", "");

            output += "are strewn around\n";
        }

        if (characterMap.getSize() > 0) {
            output += "\nSome curious faces are peering at you. You spot...\n";
            for (String item : characterMap.listEntities()) {
                output += characterMap.getEntity(item).getDescription() + "\n";
            }
        }

        if (playerMap.getSize() > 1) {
            output += "\nYou see some other intrepid adventurers travelling the world. They are called ";
            for (String name : playerMap.listEntities()) {
                if (!name.equals(player.getName())) {
                    output += name + " and ";
                }
            }
            output = output.replaceAll("(and )$", "");
            output += ".\n";

        }

        if (pathMap.getSize() == 0) {
            output += "\nUnfortunately you can't see any paths leading away. You are stuck here forever...\n";
        } else if (pathMap.getSize() == 1) {
            output += "\nYou see a path going to the " + pathMap.listEntities().get(0) + ".\n";
        } else {
            output += "You see a number of paths, going to the ";

            for (String item : pathMap.listEntities()) {
                output = output.concat(item + " , or a ");
            }
            output = output.replaceAll("(, or a )$", "");
            output += ".\n";

            output += "leading away.\n";

        }

        return output + "\n";
    }

}