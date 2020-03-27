import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Builder {
    private Game game = new Game();
    private final String entityFilename;
    private final String actionFilename;

    public Builder(String entityFilename, String actionFilename) {
        this.entityFilename = entityFilename;
        this.actionFilename = actionFilename;
    }

    public Game buildGame() {
        final ArrayList<Graph> layoutGraph = loadEntitiesFile();
        final JSONObject actionJSON = loadActionsFile();

        // Would it be better to throw exceptions back to the server class?
        // Slightly weird structure to be able to check both files can be loaded. Is
        // there a better way of doing this?
        if (layoutGraph == null || actionJSON == null) {
            System.exit(1);
        }

        try {
            processEntities(layoutGraph);
            processActions(actionJSON);
        } catch (IllegalArgumentException e) {
            System.err.println(e);
            System.exit(1);
        }

        return game;
    }

    private ArrayList<Graph> loadEntitiesFile() {
        try {
            if (!entityFilename.matches(".+(.dot)$")) {
                throw new FileNotFoundException(entityFilename + ": Invalid file extension. Expected .dot\n");
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
            return null;
        }

        // Done like this so it auto closes
        try (FileReader reader = new FileReader(entityFilename)) {
            Parser parser = new Parser();

            parser.parse(reader);
            ArrayList<Graph> layoutGraph = parser.getGraphs();

            return layoutGraph;

        } catch (ParseException | IOException e) {
            System.err.println(e);
        }

        return null;
    }

    private void processEntities(ArrayList<Graph> layoutGraph) throws IllegalArgumentException {
        if (!layoutGraph.get(0).getId().getId().equals("layout")) {
            throw new IllegalArgumentException("Expected 'layout' graph in .dot file\n");
        }

        ArrayList<Graph> entities = layoutGraph.get(0).getSubgraphs();

        ArrayList<Graph> locationsGraphArray = getGraphByName(entities, "locations").getSubgraphs();
        ArrayList<Edge> pathsEdgesArray = getGraphByName(entities, "paths").getEdges();

        if (locationsGraphArray == null || pathsEdgesArray == null) {
            throw new IllegalArgumentException("Expected a locations and paths graph in .dot file\n");
        }

        for (Graph l : locationsGraphArray) {
            Location location = buildLocation(l);
            game.getLocationMap().addEntity(location);
            if (game.getStartLocation() == null) {
                game.setStartLocation(location);
            }
        }

        for (Edge p : pathsEdgesArray) {
            String source = p.getSource().getNode().getId().getId();
            String target = p.getTarget().getNode().getId().getId();

            Location location = game.getLocationMap().getEntity(source);
            Location path = game.getLocationMap().getEntity(target);

            if (location == null || path == null) {
                throw new IllegalArgumentException("Path to non-existent location specified in .dot file\n");
            }

            location.addEntity(path);
        }
    }

    private Location buildLocation(Graph locationGraph) throws IllegalArgumentException {
        Node locationNode = locationGraph.getNodes(false).get(0);

        String id = locationNode.getId().getId();
        String description = locationNode.getAttribute("description");

        Location location = new Location(id, description);

        ArrayList<Graph> entities = locationGraph.getSubgraphs();

        for (Graph g : entities) {
            String entityName = g.getId().getId();
            ArrayList<Node> entityNodes = g.getNodes(false);
            for (Node n : entityNodes) {
                String nodeId = n.getId().getId();
                String nodeDescription = n.getAttribute("description");

                // What if an entity gets added to the game? Should this code be somewhere else?
                // Can this be made into a factory? Can't figure out how to modify return type
                // based on string
                switch (entityName) {
                    case "artefacts":
                        location.addEntity(new Artefact(nodeId, nodeDescription));
                        break;
                    case "furniture":
                        location.addEntity(new Furniture(nodeId, nodeDescription));
                        break;
                    case "characters":
                        location.addEntity(new Character(nodeId, nodeDescription));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid entity in .dot file\n");
                }
            }
        }

        return location;
    }

    private Graph getGraphByName(ArrayList<Graph> graphs, String name) {
        return graphs.stream().filter(s -> name.equals(s.getId().getId())).findAny().orElse(null);
    }

    private JSONObject loadActionsFile() {
        try {
            if (!actionFilename.matches(".+(.json)$")) {
                throw new FileNotFoundException(actionFilename + ": Invalid file extension. Expected .json\n");
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
            return null;
        }

        try (FileReader reader = new FileReader(actionFilename)) {
            JSONParser parser = new JSONParser();

            return (JSONObject) parser.parse(reader);

        } catch (IOException | org.json.simple.parser.ParseException e) {
            System.err.println(e);
        }

        return null;
    }

    private void processActions(JSONObject actionJSON) throws IllegalArgumentException {
        JSONArray actionsList = (JSONArray) actionJSON.get("actions");
        if (actionsList == null) {
            throw new IllegalArgumentException("Could not find 'actions' object in JSON\n");
        }

        for (Object a : actionsList) {
            Action action = buildAction((JSONObject) a);
            ArrayList<String> triggers = action.getTriggers();

            // Add one entry per triger to world.
            for (String t : triggers) {
                game.addAction(t, action);
            }
        }

    }

    private Action buildAction(JSONObject object) {
        ArrayList<String> triggers = convertToStringArray((JSONArray) object.get("triggers"));
        ArrayList<String> subjects = convertToStringArray((JSONArray) object.get("subjects"));
        ArrayList<String> consumed = convertToStringArray((JSONArray) object.get("consumed"));
        ArrayList<String> produced = convertToStringArray((JSONArray) object.get("produced"));
        String narration = (String) object.get("narration");

        return new Action(triggers, subjects, consumed, produced, narration);
    }

    // Could this be done as a stream?
    private ArrayList<String> convertToStringArray(JSONArray jsonArray) {
        ArrayList<String> arrayList = new ArrayList<>();

        for (Object s : jsonArray) {
            arrayList.add((String) s);
        }
        return arrayList;
    }
}