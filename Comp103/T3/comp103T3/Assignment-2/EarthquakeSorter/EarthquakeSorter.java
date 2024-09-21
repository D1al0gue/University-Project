// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2023T3, Assignment 2
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;

/**
 * EarthquakeSorter
 * Sorts data about a collection of 4335 NZ earthquakes from May 2016 to May 2017
 * Each line of the file "earthquake-data.txt" has a description of one earthquake:
 *   ID time longitude latitude magnitude depth region
 * Data is from http://quakesearch.geonet.org.nz/
 *  Note the earthquakes' ID have been modified to suit this assignment.
 *  Note bigearthquake-data.txt has just the 421 earthquakes of magnitude 4.0 and above
 *   which may be useful for testing, since it is not as big as the full file.
 *   
 */

public class EarthquakeSorter{

    private List<Earthquake> earthquakes = new ArrayList<Earthquake>();

    /**
     * Load data from the specified data file into the earthquakes field:
     */
    public void loadData(String filename) {
        try {
            Scanner sc = new Scanner(new File(filename));
    
            while (sc.hasNext()) {
                String id = sc.next();
                String date = sc.next();
                String time = sc.next();
                double longitude = sc.nextDouble();
                double latitude = sc.nextDouble();
                double magnitude = sc.nextDouble();
                double depth = sc.nextDouble();
                String region = sc.next();
    
                Earthquake quake = new Earthquake(id, date, time, longitude, latitude, magnitude, depth, region);
                earthquakes.add(quake);
            }
    
            sc.close(); // Close the Scanner after reading the file
    
            UI.printf("Loaded %d earthquakes into the list\n", earthquakes.size());
            UI.println("----------------------------");
        } catch (IOException e) {
            UI.println("File reading failed");
        }
    }

    /**
     * Sorts the earthquakes by ID
     */
    public void sortByID(){
        UI.clearText();
        UI.println("Earthquakes sorted by ID");
        /*# YOUR CODE HERE */
        Collections.sort(earthquakes);
        for (Earthquake e : this.earthquakes){
            UI.println(e);
        }
        UI.println("------------------------");
    }


    /**
     * Sorts the earthquakes by magnitude, largest first
     */
    public void sortByMagnitude(){
        UI.clearText();
        UI.println("Earthquakes sorted by magnitude (largest first)");
        /*# YOUR CODE HERE */
        Comparator<Earthquake> magnitudeComparator = Comparator.comparing(Earthquake::getMagnitude).reversed();
        Collections.sort(earthquakes, magnitudeComparator);
        for (Earthquake e : this.earthquakes){
            UI.println(e);
        }
        UI.println("------------------------");
    }


    /**
     * Sorts the list of earthquakes according to the date and time that they occurred.
     */
    public void sortByTime(){
        UI.clearText();
        UI.println("Earthquakes sorted by time");
        /*# YOUR CODE HERE */
        Comparator<Earthquake> timeComparator = Comparator.comparing(Earthquake::getDate).thenComparing(Earthquake::getTime);
        Collections.sort(earthquakes, timeComparator);
        for (Earthquake e : this.earthquakes){
            UI.println(e);
        }
        UI.println("------------------------");
    }

    /**
     * Sorts the list of earthquakes according to region. If two earthquakes have the same
     *   region, they should be sorted by magnitude (highest first) and then depth (more shallow first)
     */
    public void sortByRegion(){
        UI.clearText();
        UI.println("Earthquakes sorted by region, then by magnitude and depth");
        /*# YOUR CODE HERE */
        Comparator<Earthquake> regionComparator = Comparator.comparing(Earthquake::getRegion)
        .thenComparing(Earthquake::getMagnitude, Comparator.reverseOrder())
        .thenComparing(Earthquake::getDepth);
        Collections.sort(earthquakes, regionComparator);

        for (Earthquake e : this.earthquakes){
            UI.println(e);
        }
        UI.println("------------------------");
    }

    /**
     * Sorts the earthquakes by proximity to a specified location
     */
    public void sortByProximity(double longitude, double latitude){
        UI.clearText();
        UI.println("Earthquakes sorted by proximity");
        UI.println("Longitude: " + longitude + " Latitude: " + latitude );
        /*# YOUR CODE HERE */
        Comparator<Earthquake> proximityComparator = Comparator.comparing(e -> e.distanceTo(longitude, latitude));
        Collections.sort(earthquakes, proximityComparator);
        UI.println("------------------------");
    }

    /**
     * Add the buttons
     */
    public void setupGUI(){
        UI.initialise();
        UI.addButton("Load", this::loadData);
        UI.addButton("sort by ID",  this::sortByID);
        UI.addButton("sort by Magnitude",  this::sortByMagnitude);
        UI.addButton("sort by Time",  this::sortByTime);
        UI.addButton("sort by Region", this::sortByRegion);
        UI.addButton("sort by Proximity", this::sortByProximity);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(900,400);
        UI.setDivider(1.0);  //text pane only 
    }

    public static void main(String[] arguments){
        EarthquakeSorter obj = new EarthquakeSorter();
        obj.setupGUI();

        //WRITE HERE WHICH PARTS OF THE ASSIGNMENT YOU HAVE COMPLETED
        // so the markers know what to look for.
        UI.println("""
         Replace this text (in the main method):
         CORE:
         CompareTo method (for ID)
         Load file data 
         Sort by ID
         Sort by magnitude 

         Completion:
         sortByTime       
         sortByRegion

         Challenge: 
         Sort by Proximity
         --------------------
         """);
 
    }   

    public void loadData(){
        this.loadData(UIFileChooser.open("Choose data file"));
    }

    public void sortByProximity(){
        UI.clearText();
        this.sortByProximity(UI.askDouble("Give longitude: "), UI.askDouble("Give latitude: "));
    }

}
