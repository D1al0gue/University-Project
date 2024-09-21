// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2023T3, Assignment 5
 * Name:
 * Username:
 * ID:
 */

import ecs100.UI;
import java.awt.*;
import java.util.*;
import java.util.Queue;

/**
 * Search for paths to the goal in a maze.
 * The maze consists of a graph of MazeCells:
 *  Each cell has a collection of neighbouring cells.
 *  Each cell can be "visited" and it will remember that it has been visited
 *  A MazeCell is Iterable, so that you can iterate through its neighbour cells with
 *    for(MazeCell neighbour : cell){....
 *
 * The maze has a goal cell (shown in green, two thirds towards the bottom right corner)
 * The maze.getGoal() method returns the goal cell of the maze.
 * The user can click on a cell, and the program will search for a path
 * from that cell to the goal.
 * 
 * Every cell that is looked at during the search is coloured  yellow, and then,
 * if the cell turns out to be on a dead end, it is coloured red.
 */

public class MazeSearch {

    private Maze maze;
    private String search = "first";   // "first", "all", or "shortest"
    private int pathCount = 0;
    private boolean stopNow = false;

    /** CORE
     * Search for a path from a cell to the goal.
     * Return true if we got to the goal via this cell (and don't
     *  search for any more paths).
     * Return false if there is not a path via this cell.
     * 
     * If the cell is the goal, then we have found a path - return true.
     * If the cell is already visited, then abandon this path - return false.
     * Otherwise,
     *  Mark the cell as visited, and colour it yellow [and pause: UI.sleep(delay);]
     *  Recursively try exploring from the cell's neighbouring cells, returning true
     *   if a neighbour leads to the goal
     *  If no neighbour leads to a goal,
     *    colour the cell red (to signal failure)
     *    abandon the path - return false.
     */
    public boolean exploreFromCell(MazeCell cell) {
        if (cell == maze.getGoal()) {
            cell.draw(Color.blue);   // to indicate finding the goal
            return true;
        }
        /*# YOUR CODE HERE */
        if (cell.isVisited()){
            return false; //don't visit this cell if it has been visited before
        }

        cell.visit();
        cell.draw(Color.yellow);
        UI.sleep(delay);

        for (MazeCell neighbour : cell) {
            if (exploreFromCell(neighbour)){
                return true; //if a neighbour goes to the blue cell, return true
            }
        }

        cell.draw(Color.red);
        return false;
        }

    /** COMPLETION
     * Search for all paths from a cell,
     * If we reach the goal, then we have found a complete path,
     *  so pause for 1000 milliseconds 
     * Otherwise,
     *  visit the cell, and colour it yellow [and pause: UI.sleep(delay);]
     *  Recursively explore from the cell's neighbours, 
     *  then unvisit the cell and colour it white.
     * 
     */
    public void exploreFromCellAll(MazeCell cell) {
        if (stopNow) { return; }    // exit if user clicked the stop now button
        /*# YOUR CODE HERE */
        if (cell == maze.getGoal()) {
            cell.draw(Color.blue);
            UI.sleep(1000);
            return;
        } else if (!cell.isVisited()){
                cell.visit();
                cell.draw(Color.yellow);
                UI.sleep(delay);
            
                for (MazeCell neighbour : cell){
                    exploreFromCellAll(neighbour);
                }
                cell.unvisit(); 
                cell.draw(Color.white); //unvisit the cell after
            }    
            
            
        }

    /** CHALLENGE
     * Search for shortest path from a cell,
     * Use Breadth first search.
     */
    public void exploreFromCellShortest(MazeCell start) {
        /*# YOUR CODE HERE */
        if (stopNow) {return;}
        Queue<MazeCell> queue = new LinkedList<>();
        Map<MazeCell, MazeCell> parentMap = new HashMap<>();

        queue.add(start);

        while (!queue.isEmpty()) {
            MazeCell currentCell = queue.poll();

            // Check if the current cell is the goal
            if (currentCell == maze.getGoal()) {
                reconstructAndColorPath(parentMap, currentCell);
                UI.sleep(1000);
                return; // Stop the search once the goal is found
            }

            // Explore all unvisited neighboring cells
            for (MazeCell neighbor : currentCell) {
                if (!neighbor.isVisited()) {
                    queue.add(neighbor);
                    neighbor.visit(); // Mark the neighbor cell as visited
                    parentMap.put(neighbor, currentCell); // Store the parent cell
                }
            }
        }

        // If the loop finishes and the goal is not reached, there is no path.
        UI.println("No path found.");
    }

private void reconstructAndColorPath(Map<MazeCell, MazeCell> parentMap, MazeCell goal) {
    MazeCell current = goal;
    while (parentMap.containsKey(current)) {
        current = parentMap.get(current);
        current.draw(Color.yellow); // Color the path cells as blue
    }
    // Color the start cell as blue
    maze.getGoal().draw(Color.blue); // Goal cell is colored green
}


    //=================================================

    // fields for gui.
    private int delay = 20;
    private int size = 10;
    /**
     * Set up the interface
     */
    public void setupGui(){
        UI.addButton("New Maze", this::makeMaze);
        UI.addSlider("Maze Size", 4, 40,10, (double v)->{size=(int)v;});
        UI.setMouseListener(this::doMouse);
        UI.addButton("First path",    ()->{search="first";});
        UI.addButton("All paths",     ()->{search="all";});
        UI.addButton("Shortest path", ()->{search="shortest";});
        UI.addButton("Stop",          ()->{stopNow=true;});
        UI.addSlider("Speed", 1, 101,80, (double v)->{delay=(int)(100-v);});
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.);
    }

    /**
     * Creates a new maze and draws it .
     */
    public void makeMaze(){
        maze = new Maze(size);
        maze.draw();
    }

    /**
     * Clicking the mouse on a cell should make the program
     * search for a path from the clicked cell to the goal.
     */
    public void doMouse(String action, double x, double y){
        if (action.equals("released")){
            maze.reset();
            maze.draw();
            pathCount = 0;
            MazeCell start = maze.getCellAt(x, y);
            if (search=="first"){
                exploreFromCell(start);
            }
            else if (search=="all"){
                stopNow=false;
                exploreFromCellAll(start);
            }
            else if (search=="shortest"){
                exploreFromCellShortest(start);
            }
        }
    }

    /**
     * Construct new MazeSearch object, call setupGUI and make a maze
     */
       public static void main(String[] args) {
        MazeSearch ms = new MazeSearch();
        ms.setupGui();
        ms.makeMaze();

        //WRITE HERE WHICH PARTS OF THE ASSIGNMENT YOU HAVE COMPLETED
        // so the markers know what to look for.
        UI.println("""
         Replace this text (in the main method):
         Core

         Completion

         Challenge
      
         --------------------
         """);
       
       }
}

