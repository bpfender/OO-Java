import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Edge;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

/**
 * World
 */
public class Builder {
    // FIXME try catch exception, other error handling

    public static void main(String[] args) {
        World world = new World();

        ArrayList<Graph> graphs = parseFile(args[0]);

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
    }

    private static ArrayList<Graph> parseFile(String entities) {
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(entities);
            parser.parse(reader);
            ArrayList<Graph> graphs = parser.getGraphs();

            // QUESTION why am i calling getId twice?
            if (graphs.get(0).getId().getId().equals("layout")) {
                return graphs.get(0).getSubgraphs();
            } else {
                System.out.println("An error occurred: Not a valid layout file\n");
            }

        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe);
        } catch (ParseException pe) {
            System.out.println(pe);
        }
        /* FIXME handling of error, what do i do? */
        return null;
    }

    // FIXME basically the same functions below
    private static ArrayList<Graph> getLocationsGraph(ArrayList<Graph> graphs) {
        // FIXME handle null return and also orelse/null?
        return graphs.stream().filter(s -> "locations".equals(s.getId().getId())).findAny().orElse(null).getSubgraphs();
    }

    private static ArrayList<Edge> getPathsGraph(ArrayList<Graph> graphs) {
        return graphs.stream().filter(s -> "paths".equals(s.getId().getId())).findAny().orElse(null).getEdges();
    }

    private static Location parseLocation(Graph locationGraph) {
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

}