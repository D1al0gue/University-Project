// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2023T2, Assignment 6
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class BusNetworks {

    /** Map of towns, indexed by their names */
    private Map<String,Town> busNetwork = new HashMap<String,Town>();

    /** CORE
     * Loads a network of towns from a file.
     * Constructs a Set of Town objects in the busNetwork field
     * Each town has a name and a set of neighbouring towns
     * First line of file contains the names of all the towns.
     * Remaining lines have pairs of names of towns that are connected.
     */
    public void loadNetwork(String filename) {
        try {
            busNetwork.clear();
            UI.clearText();
            List<String> lines = Files.readAllLines(Path.of(filename));
            String firstLine = lines.remove(0);

            // Split the first line into town names and create Town objects for each town
            String[] townNames = firstLine.split(" ");
            for (String name : townNames) {
                busNetwork.put(name, new Town(name));
            }

            // Connect towns based on the remaining lines
            for (String line : lines) {
                String[] townPair = line.split(" ");
                String town1Name = townPair[0];
                String town2Name = townPair[1];
                Town town1 = busNetwork.get(town1Name);
                Town town2 = busNetwork.get(town2Name);

                if (town1 != null && town2 != null) {
                    town1.addNeighbour(town2);
                    town2.addNeighbour(town1);
                }
            }

            UI.println("Loaded " + busNetwork.size() + " towns:");

        } catch (IOException e) {
            throw new RuntimeException("Loading data.txt failed" + e);
        }
    }

    /**  CORE
     * Print all the towns and their neighbours:
     * Each line starts with the name of the town, followed by
     *  the names of all its immediate neighbours,
     */
    public void printNetwork() {
        UI.println("The current network: \n====================");
        /*# YOUR CODE HERE */

        for (Town town : busNetwork.values()) {
            UI.print(town.getName() + "-> ");
            Set<Town> neighbours = town.getNeighbours();
            for (Town neighbour : neighbours) {
                UI.print(neighbour.getName() + " ");
            }
            UI.println();
        }
    }

    /** COMPLETION
     * Return a set of all the nodes that are connected to the given node.
     * Traverse the network from this node in the standard way, using a
     * visited set, and then return the visited set
     */
    // Recursive helper method for findAllConnected
    private void findConnectedRecursive(Town town, Set<Town> visited) {
    visited.add(town);
    for (Town neighbour : town.getNeighbours()) {
        if (!visited.contains(neighbour)) {
            findConnectedRecursive(neighbour, visited);
        }
    }
}

    public Set<Town> findAllConnected(Town town) {
    Set<Town> visited = new HashSet<>();
    findConnectedRecursive(town, visited);
    return visited;
    }

    /**  COMPLETION
     * Print all the towns that are reachable through the network from
     * the town with the given name.
     * Note, do not include the town itself in the list.
     */
    public void printReachable(String name){
        Town town = busNetwork.get(name);
        if (town==null){
            UI.println(name+" is not a recognised town");
        }
        else {
            UI.println("\nFrom "+town.getName()+" you can get to:");
            /*# YOUR CODE HERE */
            Set<Town> reachableTowns = findAllConnected(town);
            reachableTowns.remove(town); // Remove the own town from the list
            for (Town reachableTown : reachableTowns) {
                UI.println(reachableTown.getName());
            }
        }
    }

    

    /**  COMPLETION
     * Print all the connected sets of towns in the busNetwork
     * Each line of the output should be the names of the towns in a connected set
     * Works through busNetwork, using findAllConnected on each town that hasn't
     * yet been printed out.
     */
    public void printConnectedGroups() {
        UI.println("Groups of Connected Towns: \n================");
        int groupNum = 1;
        /*# YOUR CODE HERE */
        Set<Town> visited = new HashSet<>();

        for (Town town : busNetwork.values()) {
            if (!visited.contains(town)) {
                Set<Town> connectedTowns = findAllConnected(town);
                visited.addAll(connectedTowns);

                UI.println("Group " + groupNum + ": " + connectedTowns);
                groupNum++;
            }
        }
    }

    /**
     * Set up the GUI (buttons and mouse)
     */
    public void setupGUI() {
        UI.addButton("Load", ()->{loadNetwork(UIFileChooser.open());});
        UI.addButton("Print Network", this::printNetwork);
        UI.addTextField("Reachable from", this::printReachable);
        UI.addButton("All Connected Groups", this::printConnectedGroups);
        UI.addButton("Clear", UI::clearText);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1100, 500);
        UI.setDivider(1.0);
        loadNetwork("data-small.txt");
    }

    // Main
    public static void main(String[] arguments) {
        BusNetworks bnw = new BusNetworks();
        bnw.setupGUI();
    }

}
