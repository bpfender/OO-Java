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

        // QUESTION graceful handling of program termination?
        if (layoutGraph == null) {
            System.exit(1);
        }

        try {
            processEntities(world, layoutGraph);
        } catch (IllegalArgumentException e) {
            System.err.println(e);
            System.exit(1);
        }

        // Populate actions
        JSONArray actions = parseActionsFile(args[1]);

        for (Object obj : actions) {
            Action action = getActions((JSONObject) obj);
            JSONArray triggers = action.getTriggers();
            // QUESTION is this the most efficient way of doing this?
            for (Object trig : triggers) {
                world.addAction((String) trig, action);
            }
        }

        return world;
    }

    // TODO add filename extension checking?
    private ArrayList<Graph> loadEntitiesFile(String filename) {
        // QUESTION is this ok throwing my own exceptions?
        try {
            if (!filename.matches(".+(.dot)$")) {
                throw new FileNotFoundException("Invalid file extension");
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
        if (layoutGraph.get(0).getId().getId().equals("layout")) {
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
                    case "character":
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

    private JSONArray parseActionsFile(String actions) {
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(actions);
            JSONObject object = (JSONObject) parser.parse(reader);
            JSONArray array = (JSONArray) object.get("actions");
            return array;

        } catch (FileNotFoundException fnfe) {
            // TODO: handle exception
        } catch (IOException io) {

        } catch (org.json.simple.parser.ParseException pe) {

        }
        return null;
        // FIXME error handling!!

    }

    // TODO check that zero length arrays are handled properly
    private Action getActions(JSONObject object) {
        JSONArray triggers = (JSONArray) object.get("triggers");
        JSONArray subjects = (JSONArray) object.get("subjects");
        JSONArray consumed = (JSONArray) object.get("consumed");
        JSONArray produced = (JSONArray) object.get("produced");
        String narration = (String) object.get("narration");

        for (Object s : triggers) {
            System.out.printf("%s ", (String) s);
        }

        return new Action(triggers, subjects, consumed, produced, narration);
    }

}