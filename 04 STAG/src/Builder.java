import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.alexmerz.graphviz.ParseException;
import com.alexmerz.graphviz.Parser;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;

/**
 * World
 */
public class Builder {
    // FIXME try catch exception, other error handling

    public static void main(String[] args) {
        ArrayList<Graph> graphs = parseFile(args[0]);
        ArrayList<Graph> locations = getLocationsGraph(graphs);
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

    private static ArrayList<Graph> getLocationsGraph(ArrayList<Graph> graphs) {
        // FIXME handle null return and also orelse/null?
        return graphs.stream().filter(s -> "locations".equals(s.getId().getId())).findAny().orElse(null).getSubgraphs();
    }

    private static void parseLocation(Graph location) {
        Node nodesLoc = location.getNodes(false).get(0);

        String name = nodesLoc.getId().getId();
        String description = nodesLoc.getAttribute("description");

        System.out.printf("Location name %s \n", name);
        System.out.printf("Location descr %s \n", description);

        ArrayList<Graph> entities = location.getSubgraphs();

        for (Graph g : entities) {
            String entName = g.getId().getId();
            ArrayList<Node> nodesEnt = g.getNodes(false);
            for (Node n : nodesEnt) {
                String nodName = n.getId().getId();
                String nodDescr = n.getAttribute("description");

                System.out.printf("%s \n", entName);
                System.out.printf("Node name %s \n", nodName);
                System.out.printf("Node descr %s \n", nodDescr);

                switch (entName) {
                    case "artefacts":
                        System.out.printf("art\n", entName);
                        break;
                    case "furniture":
                        System.out.printf("furn\n", entName);
                        break;
                    case "character":
                        System.out.printf("char\n", entName);
                        break;
                    default:
                        break;
                }
            }
        }

        // List<Character> characters = new ArrayList<Character>();
        // List<Artefact> artefacts = new ArrayList<Artefact>();
        // List<Furniture> furniture = new ArrayList<Furniture>();

    }

}