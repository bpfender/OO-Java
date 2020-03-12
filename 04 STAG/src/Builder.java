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

/**
 * World
 */
/// TODO make sure everything is lowercased

public class Builder {
    // TODO need to catch first location
    // FIXME try catch exception, other error handling

    public World buildWorld(String args[]) {
        World world = new World();

        ArrayList<Graph> layoutGraph = loadEntitiesFile(args[0]);
        JSONObject actionJSON = loadActionsFile(args[1]);

        // QUESTION graceful handling of program termination?
        if (layoutGraph == null || actionJSON == null) {
            System.exit(1);
        }

        try {
            processEntities(world, layoutGraph);
            processActions(world, actionJSON);
        } catch (IllegalArgumentException e) {
            System.err.println(e);
            System.exit(1);
        }

        return world;
    }

    // TODO add filename extension checking?
    private ArrayList<Graph> loadEntitiesFile(String filename) {
        // QUESTION is this ok throwing my own exceptions?
        try {
            if (!filename.matches(".+(.dot)$")) {
                throw new FileNotFoundException(filename + ": Invalid file extension. Expected .dot");
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
            return null;
        }

        // Done like this so it auto closes
        try (FileReader reader = new FileReader(filename)) {
            Parser parser = new Parser();

            parser.parse(reader);
            ArrayList<Graph> layoutGraph = parser.getGraphs();

            return layoutGraph;

        } catch (FileNotFoundException | ParseException e) {
            System.err.println(e);
        } catch (IOException io) {
            // QUESTION what to do here?
        }
        /* FIXME handling of error, what do i do? */
        return null;
    }

    private void processEntities(World world, ArrayList<Graph> layoutGraph) throws IllegalArgumentException {
        // QUESTION why calling getId() twice?
        if (!layoutGraph.get(0).getId().getId().equals("layout")) {
            throw new IllegalArgumentException("Expected 'layout' graph in file");
        }

        ArrayList<Graph> entities = layoutGraph.get(0).getSubgraphs();

        ArrayList<Graph> locationsGraph = getLocationsGraph(entities);
        ArrayList<Edge> pathsGraph = getPathsGraph(entities);

        if (locationsGraph == null || pathsGraph == null) {
            throw new IllegalArgumentException("Expected a locations and paths graph in file");
        }

        for (Graph l : locationsGraph) {
            Location location = processLocationGraph(l);
            world.addLocation(location.getName(), location);
        }

        for (Edge p : pathsGraph) {
            // TODO error handling of edges?
            String source = p.getSource().getNode().getId().getId();
            String target = p.getTarget().getNode().getId().getId();

            Location location = world.getLocation(source);
            Location path = world.getLocation(target);

            if (location == null || path == null) {
                throw new IllegalArgumentException("Path to non-existent location");
            }

            location.addEntity(path);
        }
    }

    // FIXME basically the same functions below, generics possible?
    private ArrayList<Graph> getLocationsGraph(ArrayList<Graph> graphs) {
        return graphs.stream().filter(s -> "locations".equals(s.getId().getId())).findAny().orElse(null).getSubgraphs();
    }

    private ArrayList<Edge> getPathsGraph(ArrayList<Graph> graphs) {
        return graphs.stream().filter(s -> "paths".equals(s.getId().getId())).findAny().orElse(null).getEdges();
    }

    private Location processLocationGraph(Graph locationGraph) throws IllegalArgumentException {
        Node locationNode = locationGraph.getNodes(false).get(0);

        String name = locationNode.getId().getId();
        String description = locationNode.getAttribute("description");

        Location location = new Location(name, description);

        ArrayList<Graph> entities = locationGraph.getSubgraphs();

        for (Graph g : entities) {
            String entityName = g.getId().getId();
            ArrayList<Node> entityNodes = g.getNodes(false);
            for (Node n : entityNodes) {
                String nodeName = n.getId().getId();
                String nodeDescription = n.getAttribute("description");

                // What if an entity gets added to the game?
                switch (entityName) {
                    case "artefacts":
                        Artefact artefact = new Artefact(nodeName, nodeDescription);
                        location.addEntity(artefact);
                        break;
                    case "furniture":
                        Furniture furniture = new Furniture(nodeName, nodeDescription);
                        location.addEntity(furniture);
                        break;
                    case "characters":
                        Character character = new Character(nodeName, nodeDescription);
                        location.addEntity(character);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid entity in .dot file");
                }
            }
        }

        return location;
    }

    private JSONObject loadActionsFile(String filename) {
        try {
            if (!filename.matches(".+(.json)$")) {
                throw new FileNotFoundException(filename + ": Invalid file extension. Expected .json");
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
            return null;
        }

        try (FileReader reader = new FileReader(filename)) {
            JSONParser parser = new JSONParser();

            return (JSONObject) parser.parse(reader);

        } catch (FileNotFoundException | org.json.simple.parser.ParseException e) {
            System.err.println(e);
        } catch (IOException e) {

        }
        return null;
    }

    private void processActions(World world, JSONObject actionJSON) throws IllegalArgumentException {
        JSONArray actionsList = (JSONArray) actionJSON.get("actions");
        if (actionsList == null) {
            throw new IllegalArgumentException("Could not find 'actions' object in JSON");
        }

        for (Object a : actionsList) {
            Action action = buildAction((JSONObject) a);
            ArrayList<String> triggers = action.getTriggers();

            // Add one entry per triger to world.
            for (String t : triggers) {
                world.addAction(t, action);
            }
        }

    }

    // TODO check that zero length arrays are handled properly. Error handling here?
    private Action buildAction(JSONObject object) {
        ArrayList<String> triggers = convertToStringArray((JSONArray) object.get("triggers"));
        ArrayList<String> subjects = convertToStringArray((JSONArray) object.get("subjects"));
        ArrayList<String> consumed = convertToStringArray((JSONArray) object.get("consumed"));
        ArrayList<String> produced = convertToStringArray((JSONArray) object.get("produced"));
        String narration = (String) object.get("narration");

        for (String s : produced) {
            System.out.printf("%s\n", s);
        }

        return new Action(triggers, subjects, consumed, produced, narration);
    }

    // QUESTION could this be done as a stream?
    private ArrayList<String> convertToStringArray(JSONArray jsonArray) {
        ArrayList<String> arrayList = new ArrayList<>();
        // TODO how does this handle empties?
        for (Object s : jsonArray) {
            arrayList.add((String) s);
        }
        return arrayList;
    }

}