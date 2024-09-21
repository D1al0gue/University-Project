// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2023T3, Assignment 5
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
     * Constructs a Map of Town objects in the busNetwork field
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
            /*# YOUR CODE HERE */

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

        } catch (IOException e) {throw new RuntimeException("Loading data.txt failed" + e);}
    }

    /**  CORE
     * Print all the towns and their neighbours:
     * Each line starts with the name of the town, followed by
     *  the names of all its immediate neighbours,
     */
    public void printNetwork() {
        UI.println("The current network: \n====================");
        /*# YOUR CODE HERE */
        for (Town t : busNetwork.values()){
            UI.print(t.getName() + " -> ");
            Set<Town> neighbours = t.getNeighbours();
            for (Town neighbour : neighbours) {
                UI.print(neighbour.getName() + " ");
            }
            UI.println();
        }
    }

    /** CORE
     * Print out the towns on a route (not necessarily the shortest)
     * from a starting town to a destination town.
     * OK to print the towns on the route in reverse order.
     * Use a recursive (post-order) depth first search.
     * Use a helper method with a visited set.
     */
    public void findRoute(Town town, Town dest) {
        UI.println("Looking for route between "+town.getName()+" and "+dest.getName()+":");
        /*# YOUR CODE HERE */
        Set<Town> visited = new HashSet<>();
        if (!findRouteHelper(town, dest, visited)) {
        UI.println("No route found.");
    }
        
    }
    
    private boolean findRouteHelper(Town current, Town dest, Set<Town> visited) {
        if (current == dest) {
            UI.println(current.getName());
            return true;
        }
    
        visited.add(current);
        for (Town neighbour : current.getNeighbours()) {
            if (!visited.contains(neighbour)) {
                if (findRouteHelper(neighbour, dest, visited)) {
                    UI.println(current.getName());
                    return true;
                }
            }
        }
        return false;
    }

    /**  COMPLETION
     * Print all the towns that are reachable through the network from
     * the given town. The Towns should be printed in order of distance from the town
     * where distance is the number of stops along the way.
     */
    public void printReachable(Town town){
        UI.println("\nFrom "+town.getName()+" you can get to:");
        /*# YOUR CODE HERE */
        Map<Town, Integer> distances = new HashMap<>();
        Queue<Town> queue = new LinkedList<>();
        queue.add(town);
        distances.put(town, 0);

        while (!queue.isEmpty()) {
            Town current = queue.poll();
            int distance = distances.get(current);

            for (Town neighbour : current.getNeighbours()) {
                if (!distances.containsKey(neighbour)) {
                    distances.put(neighbour, distance + 1);
                    queue.add(neighbour);
                }
            }
        }

        distances.remove(town);
        distances.forEach((neighbour, distance) -> UI.println(town.getName() + " (Distance: " + distance + ")"));
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
            if(!visited.contains(town)){
                Set<Town> connectedTowns = findAllConnected(town);
                visited.addAll(connectedTowns);

                UI.println("Group " + groupNum + ": " + connectedTowns);
                groupNum++;
            }
        }
    }

    // Suggested helper method for printConnectedGroups
    /**
     * Return a set of all the nodes that are connected to the given node.
     * Traverse the network from this node using a
     * visited set, and then return the visited set. 
     */
    public Set<Town> findAllConnected(Town town) {
        /*# YOUR CODE HERE */
        Set<Town> visited = new HashSet<>();
        findConnectedRecursive(town, visited);
        return visited;
    }

    private void findConnectedRecursive(Town town, Set<Town> visited) {
        visited.add(town);
        for (Town neighbour : town.getNeighbours()) {
            if (!visited.contains(neighbour)) {
                findConnectedRecursive(neighbour, visited);
            }
        }
    }
    

    /**
     * Set up the GUI (buttons and mouse)
     */
    public void setupGUI() {
        UI.addButton("Load", ()->{loadNetwork(UIFileChooser.open());});
        UI.addButton("Print Network", this::printNetwork);
        UI.addButton("Find Route", () -> {findRoute(askTown("From"), askTown("Destination"));});
        UI.addButton("All Reachable", () -> {printReachable(askTown("Town"));});
        UI.addButton("All Connected Groups", this::printConnectedGroups);
        UI.addButton("Clear", UI::clearText);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(1100, 500);
        UI.setDivider(1.0);
        loadNetwork("data-small.txt");
    }

    // Main
    public static void main(String[] arguments) {
        new BusNetworks().setupGUI();

        //WRITE HERE WHICH PARTS OF THE ASSIGNMENT YOU HAVE COMPLETED
        // so the markers know what to look for.
        UI.println("""
         Replace this text (in the main method):
         
         Core

         Completion


      
         --------------------
         """);

    }

    // Utility method
    /**
     * Method to get a Town from a dialog box with a list of options
     */
    public Town askTown(String question){
        Object[] possibilities = busNetwork.keySet().toArray();
        Arrays.sort(possibilities);
        String townName = (String)javax.swing.JOptionPane.showInputDialog
            (UI.getFrame(),
                question, "",
                javax.swing.JOptionPane.PLAIN_MESSAGE,
                null,
                possibilities,
                possibilities[0].toString());
        return busNetwork.get(townName);
    }

}
