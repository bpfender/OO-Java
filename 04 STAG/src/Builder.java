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

        ArrayList<Graph> graphs = loadEntitiesFile(args[0]);

        // Populate locations
        ArrayList<Graph> locations = getLocationsGraph(graphs);
        for (Graph loc : locations) {
            Location location = parseLocation(loc);
            world.addLocation(location.getName(), location);
        }

        // Populate paths
        ArrayList<Edge> paths = getPathsGraph(graphs);
        for (Edge p : paths) {
            String source = p.getSource().getNode().getId().getId();
            String target = p.getTarget().getNode().getId().getId();

            Location location = world.getLocation(source);
            Location path = world.getLocation(target);

            location.addEntity(path);
        }

        System.out.printf("TEST %s\n\n", locations.get(0).getId());
        parseLocation(locations.get(0));

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
        // QUESTION is this ok?
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
            ArrayList<Graph> entitiesGraph = parser.getGraphs();

            // FIXME make sure that readers are closed properly
            return entitiesGraph;
            // QUESTION why am i calling getId twice?
            /*
             * if (graphs.get(0).getId().getId().equals("layout")) { return
             * graphs.get(0).getSubgraphs(); } else {
             * System.out.println("An error occurred: Not a valid layout file\n"); }
             */

        } catch (FileNotFoundException | ParseException e) {
            System.err.println(e);
        } catch (IOException io) {
            // QUESTION what to do here?
        }
        /* FIXME handling of error, what do i do? */
        return null;
    }

    // FIXME basically the same functions below
    private ArrayList<Graph> getLocationsGraph(ArrayList<Graph> graphs) {
        // FIXME handle null return and also orelse/null?
        return graphs.stream().filter(s -> "locations".equals(s.getId().getId())).findAny().orElse(null).getSubgraphs();
    }

    private ArrayList<Edge> getPathsGraph(ArrayList<Graph> graphs) {
        return graphs.stream().filter(s -> "paths".equals(s.getId().getId())).findAny().orElse(null).getEdges();
    }

    private Location parseLocation(Graph locationGraph) {
        Node nodesLoc = locationGraph.getNodes(false).get(0);

        String name = nodesLoc.getId().getId();
        String description = nodesLoc.getAttribute("description");

        Location location = new Location(name, description);

        ArrayList<Graph> entities = locationGraph.getSubgraphs();

        for (Graph g : entities) {
            String entName = g.getId().getId();
            ArrayList<Node> nodesEnt = g.getNodes(false);
            for (Node n : nodesEnt) {
                String nodName = n.getId().getId();
                String nodDescr = n.getAttribute("description");

                switch (entName) {
                    case "artefacts":
                        Artefact artefact = new Artefact(nodName, nodDescr);
                        location.addEntity(artefact);
                        break;
                    case "furniture":
                        Furniture furniture = new Furniture(nodName, nodDescr);
                        location.addEntity(furniture);
                        break;
                    case "character":
                        Character character = new Character(nodName, nodDescr);
                        location.addEntity(character);
                        break;
                    default:
                        break;
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