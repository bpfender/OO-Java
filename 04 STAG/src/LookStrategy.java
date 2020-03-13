import java.util.ArrayList;

public class LookStrategy implements CommandStrategy {
    Location location;

    public LookStrategy(Location location) {
        this.location = location;
    }

    @Override
    public String process() {
        String locationDescription = new String();
        ArrayList<String> furniture = location.getFurnitureList();
        ArrayList<String> artefacts = location.getArtefactList();
        ArrayList<String> characters = location.getCharacterList();
        ArrayList<String> paths = location.getPathList();

        locationDescription.concat(location.getDescription() + "\n");

        if (furniture.size() != 0) {
            locationDescription.concat("You see\n");

            for (String item : furniture) {
                locationDescription.concat(location.getEntityDescription(item) + "\n");
            }
        }

        if (artefacts.size() == 1) {
            locationDescription.concat("There's a " + artefacts.get(0) + " lying on the ground.\n");
        } else {
            locationDescription.concat("Some items, including a ");
            for (String item : artefacts) {
                locationDescription.concat(item + ", ");
            }
            locationDescription.concat("are strewn around\n");
        }

        if (characters.size() != 0) {
            locationDescription.concat("Some curious faces peer at you. You spot\n");
            for (String item : characters) {
                locationDescription.concat(location.getEntityDescription(item) + "\n");
            }
        }

        if (paths.size() == 0) {
            locationDescription
                    .concat("Unfortunately you can't see any paths leading away. You are stuck here forever...");
        } else {
            locationDescription.concat("You see a number of path. going to ");

            for (String item : paths) {
                locationDescription.concat(item + ", ");
            }
            locationDescription.concat("leading away.\n");
        }
        return null;
    }

}