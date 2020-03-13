import java.util.ArrayList;

public class LookStrategy implements CommandStrategy {
    Location location;

    public LookStrategy(Location location) {
        this.location = location;
    }

    @Override
    public String process() {
        String locationDescription = new String("TEST");
        ArrayList<String> furniture = location.getFurnitureList();
        ArrayList<String> artefacts = location.getArtefactList();
        ArrayList<String> characters = location.getCharacterList();
        ArrayList<String> paths = location.getPathList();

        System.out.println(furniture.size());
        System.out.println(artefacts);
        System.out.println(characters);
        System.out.println(paths);

        locationDescription = locationDescription.concat(location.getDescription() + "\n");

        if (furniture.size() != 0) {
            locationDescription = locationDescription.concat("You see around you a...\n");

            for (String item : furniture) {
                locationDescription = locationDescription.concat(location.getEntityDescription(item) + "\n");
            }
        }

        if (artefacts.size() == 1) {
            locationDescription = locationDescription
                    .concat("There's a " + artefacts.get(0) + " lying on the ground.\n");
        } else if (artefacts.size() > 1) {
            locationDescription = locationDescription.concat("Some items, including a ");
            for (String item : artefacts) {
                locationDescription.concat(item + ", ");
            }
            locationDescription = locationDescription.concat("are strewn around\n");
        }

        if (characters.size() != 0) {
            locationDescription = locationDescription.concat("Some curious faces peer at you. You spot\n");
            for (String item : characters) {
                locationDescription = locationDescription.concat(location.getEntityDescription(item) + "\n");
            }
        }

        if (paths.size() == 0) {
            locationDescription = locationDescription
                    .concat("Unfortunately you can't see any paths leading away. You are stuck here forever...");
        } else if (paths.size() == 1) {
            locationDescription = locationDescription.concat("You see a path going to the " + paths.get(0) + ".\n");
        } else {
            locationDescription = locationDescription.concat("You see a number of paths, going to a");

            for (String item : paths) {
                locationDescription = locationDescription.concat(item + ", or a ");
            }
            locationDescription = locationDescription.concat("leading away.\n");

        }

        return locationDescription;
    }

}